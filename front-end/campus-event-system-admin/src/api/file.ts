import axios from 'axios';

export function uploadFile(
  data: FormData,
  usage: string,
  uploadProgress: (event: any) => void = () => {}
) {
  return axios.post('/api/file/upload', data, {
    params: {
      usage,
    },
    onUploadProgress: uploadProgress,
  });
}

export function getFile(
  data: FormData,
  usage: string,
  uploadProgress: (event: any) => void = () => {}
) {
  return axios.post('/api/file/upload', data, {
    params: {
      usage,
    },
    onUploadProgress: uploadProgress,
  });
}

export async function ImageInterceptor(url: string) {
  // 请求图片，axios是已经实现拦截的,如上面导入,responseType为arraybuffer
  const response = await axios.get(url, { responseType: 'arraybuffer' });
  const str = btoa(
    new Uint8Array(response.data).reduce(
      (data, byte) => data + String.fromCharCode(byte),
      ''
    )
  );
  const res = `data:image/png;base64,${str}`;
  return res;
}
