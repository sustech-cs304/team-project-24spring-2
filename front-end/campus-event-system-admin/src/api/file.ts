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
