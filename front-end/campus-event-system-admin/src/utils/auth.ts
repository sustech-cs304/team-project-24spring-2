const TOKEN_KEY = 'token';
const TOKEN_EXPIRE = 'token_expire';

const isLogin = () => {
  return !!localStorage.getItem(TOKEN_KEY);
};

const getToken = () => {
  return {
    token: localStorage.getItem(TOKEN_KEY),
    expire: localStorage.getItem(TOKEN_EXPIRE),
  };
};

const setToken = (token: string, expire: number) => {
  localStorage.setItem(TOKEN_KEY, token);
  localStorage.setItem(TOKEN_EXPIRE, expire.toString());
};

const clearToken = () => {
  localStorage.removeItem(TOKEN_KEY);
  localStorage.removeItem(TOKEN_EXPIRE);
};



export { isLogin, getToken, setToken, clearToken };
