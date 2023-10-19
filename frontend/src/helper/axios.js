import axios from 'axios';
import { TOKEN_BEGIN } from '../constants/tokenBegin';

let REACT_APP_API_BASE_URL = 'http://localhost:8080/';
if (process.env.REACT_APP_API_BASE_URL !== undefined) {
    REACT_APP_API_BASE_URL = process.env.REACT_APP_API_BASE_URL.trim();
}
const instance = axios.create({
    baseURL: REACT_APP_API_BASE_URL,
});

const token = localStorage.getItem('token');
if (token && token.includes(TOKEN_BEGIN)) {
    instance.defaults.headers.common.Authorization = token;
}

export default instance;
