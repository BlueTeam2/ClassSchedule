import { call, put } from 'redux-saga/effects';
import { setCurrentSemester, setOpenSnackbar, setSemesterLoading } from '../../actions';
import { CURRENT_SEMESTER_URL } from '../../constants/axios';
import { snackbarTypes } from '../../constants/snackbarTypes';
import { axiosCall } from '../../services/axios';
import i18n from '../../i18n';
import { NO_CURRENT_SEMESTER_ERROR } from '../../constants/translationLabels/common';

export function* getCurrentSemester() {
    try {
        const response = yield call(axiosCall, CURRENT_SEMESTER_URL);
        yield put(setCurrentSemester(response.data));
        yield put(setSemesterLoading(false));
    } catch (error) {
        const message = i18n.t(NO_CURRENT_SEMESTER_ERROR);
        const isOpen = true;
        const type = snackbarTypes.ERROR;
        yield put(setOpenSnackbar({ isOpen, type, message }));
        yield put(setSemesterLoading(false));
    }
}
