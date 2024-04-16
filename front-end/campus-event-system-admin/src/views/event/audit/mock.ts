import Mock from 'mockjs';
import qs from 'query-string';
import setupMock, { successResponseWrap } from '@/utils/setup-mock';
import { GetParams } from '@/types/global';

const { Random } = Mock;

const data = Mock.mock({
  'list|10': [
    {
      'id|8': /[A-Z][a-z][-][0-9]/,
      'number|2-3': /[0-9]/,
      'name|1': ['春日摄影', '致新夜跑', '互助课堂', '文学沙龙'],
      'contentType|1': ['callingImg', 'callingText', 'socialMeeting', 'show'],
      'supervisor|1': ['致新书院', '数学系', '个人-冯泽欣', '招生办'],
      'status|1': ['online', 'offline'],
      'filterType|1': ['artificial', 'rules'],
      'createdTime': Random.datetime(),
    },
  ],
});

setupMock({
  setup() {
    Mock.mock(new RegExp('/api/event/query'), (params: GetParams) => {
      const { current = 1, pageSize = 10 } = qs.parseUrl(params.url).query;
      const p = current as number;
      const ps = pageSize as number;
      return successResponseWrap({
        list: data.list.slice((p - 1) * ps, p * ps),
        total: 10,
      });
    });
  },
});

