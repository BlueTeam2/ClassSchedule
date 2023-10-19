import axios from '../helper/axios';

export const axiosCall = (url, method, data) => axios({ method, url, data });
