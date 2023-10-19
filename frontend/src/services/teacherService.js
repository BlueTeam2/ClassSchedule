import { store } from '../store';
import axios from '../helper/axios';
import { errorHandler } from '../helper/handlerAxios';
import { TEACHERS_WITHOUT_ACCOUNT_URL } from '../constants/axios';
import { showAllTeachersSuccess } from '../actions';

export const getTeachersWithoutAccount = () => {
    // will be replace in userService
    axios
        .get(TEACHERS_WITHOUT_ACCOUNT_URL)
        .then((response) => {
            store.dispatch(showAllTeachersSuccess(response.data));
        })
        .catch((error) => errorHandler(error));
};
