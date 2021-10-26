import { call, put } from 'redux-saga/effects';
import { setScheduleItems, setOpenSnackbar, setScheduleLoading } from '../../actions';
import { snackbarTypes } from '../../constants/snackbarTypes';
import { axiosCall } from '../../services/axios';
import i18n from '../../i18n';
import { SCHEDULE_SEMESTER_ITEMS_URL } from '../../constants/axios';

export function* getScheduleItemsBySemester({ semesterId }) {
    const requestUrl = `${SCHEDULE_SEMESTER_ITEMS_URL}?semesterId=${semesterId}`;
    try {
        const response = yield call(axiosCall, requestUrl);
        yield put(setScheduleItems(response.data));
        yield put(setScheduleLoading(false));
    } catch (error) {
        const message = error.response
            ? i18n.t(error.response.data.message, error.response.data.message)
            : 'Error';
        const isOpen = true;
        const type = snackbarTypes.ERROR;
        yield put(setOpenSnackbar({ isOpen, type, message }));
        yield put(setScheduleLoading(false));
    }
}
