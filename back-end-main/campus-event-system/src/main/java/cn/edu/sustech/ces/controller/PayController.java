package cn.edu.sustech.ces.controller;

import cn.edu.sustech.ces.entity.Order;
import cn.edu.sustech.ces.entity.User;
import cn.edu.sustech.ces.enums.OrderStatus;
import cn.edu.sustech.ces.enums.PurchaseMethod;
import cn.edu.sustech.ces.security.CESUserDetails;
import cn.edu.sustech.ces.service.OrderService;
import cn.edu.sustech.ces.service.alipay.AlipayConfig;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/pay")
public class PayController {

    private final OrderService orderService;

    private final AlipayConfig alipayConfig;

    /*
        TODO:
            refine the class
     */

    @Autowired
    public PayController(OrderService orderService, AlipayConfig alipayConfig) {
        this.orderService = orderService;
        this.alipayConfig = alipayConfig;
    }

    @PostMapping("/alipay-notify")
    public void alipayNotify(HttpServletRequest request) throws Exception {
        if (request.getParameter("trade_status").equals("TRADE_SUCCESS")) {
            Map<String, String> params = new HashMap<>();
            Map<String, String[]> requestParams = request.getParameterMap();
            requestParams.forEach((k, v) -> params.put(k, request.getParameter(k)));
            String sign = params.get("sign");
            String content = AlipaySignature.getSignCheckContentV1(params);
            boolean signVerified = AlipaySignature.rsa256CheckContent(content, sign, alipayConfig.getAlipayPublicKey(), alipayConfig.getCharset());
            if (signVerified) {
                String orderId = params.get("out_trade_no");
                Order order = orderService.getOrder(UUID.fromString(orderId));
                if (order != null) {
                    order.setStatus(OrderStatus.PAID);
                    order.setPurchaseMethod(PurchaseMethod.ALIPAY);
                    String createTime = params.get("gmt_create");
                    String finishTime = params.get("gmt_payment");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    order.setPurchaseCreateTime(dateFormat.parse(createTime).getTime());
                    order.setPurchaseFinishTime(dateFormat.parse(finishTime).getTime());
                    orderService.updateOrder(order);
                } else {
                    // TODO: maybe we can log error here?
                }
            } else {
                // TODO: maybe we can log error here?
            }
        } else {
            // TODO: maybe we can log error here?
        }
    }

    @GetMapping("/cancel")
    @PreAuthorize("isAuthenticated()")
    public void cancelOrder(@NotNull String orderId, HttpServletResponse response) {

        // TODO: user verification

        Order order = orderService.getOrder(UUID.fromString(orderId));
        if (order == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        order.setStatus(OrderStatus.CANCELED);
        orderService.updateOrder(order);
        response.setStatus(HttpServletResponse.SC_OK);
    }


    @GetMapping("/alipay")
    @PreAuthorize("isAuthenticated()")
    public void payOrderByAlipay(@NotNull String orderId, HttpServletResponse response) throws Exception {

        CESUserDetails userDetails = (CESUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userDetails.getUser();

        Order order = orderService.getOrder(UUID.fromString(orderId));
        if (order == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (!order.getPayerId().equals(user.getId())) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (order.getStatus() != OrderStatus.UNPAID) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        if (order.getPrice() == 0) {
            order.setStatus(OrderStatus.PAID);
            order.setPurchaseMethod(PurchaseMethod.CASH);
            order.setPurchaseCreateTime(System.currentTimeMillis());
            order.setPurchaseFinishTime(System.currentTimeMillis());
            orderService.updateOrder(order);
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        AlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getAlipayGatewayUrl(),
                alipayConfig.getAppId(),
                alipayConfig.getAppPrivateKey(),
                alipayConfig.getFormat(),
                alipayConfig.getCharset(),
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getSignType());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", order.getId());
        jsonObject.put("product_code", "FAST_INSTANT_TRADE_PAY");
        jsonObject.put("total_amount", order.getPrice());
        jsonObject.put("subject", order.getName());
        jsonObject.put("body", order.getTicketId());

        // TODO: set return url, order body

        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setNotifyUrl(alipayConfig.getAlipayNotifyUrl());

//        TODO: set return url
//        alipayRequest.setReturnUrl("www.baidu.com");

        alipayRequest.setBizContent(jsonObject.toString());

        String form = "";
        form = alipayClient.pageExecute(alipayRequest).getBody();
        response.setContentType("text/html;charset=" + alipayConfig.getCharset());
        response.getWriter().write(form);
        response.getWriter().flush();
        response.getWriter().close();
        response.setStatus(HttpServletResponse.SC_OK);
        return;
    }

}
