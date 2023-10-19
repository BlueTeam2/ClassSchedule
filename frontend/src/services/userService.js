import axios from '../helper/axios';
import { store } from '../store';
import {
    MERGE_USER_AND_TEACHER_URL,
    USERS_URL,
    USER_PROFILE,
    UPDATE_USER_PROFILE,
} from '../constants/axios';
import { setUsers, setUser, setTeacher } from '../actions/index';

import i18n from '../i18n';
import { setLoadingService } from './loadingService';
import { getTeachersWithoutAccount } from './teacherService';
import { errorHandler, successHandler } from '../helper/handlerAxios';
import {
    BACK_END_SUCCESS_OPERATION,
    SUCCESSFULLY_MERGED,
} from '../constants/translationLabels/serviceMessages';
import { FORM_USER_LABEL, FORM_TEACHER_LABEL } from '../constants/translationLabels/formElements';

export const getUsersService = () => {
    axios
        .get(USERS_URL)
        .then((response) => {
            store.dispatch(setUsers(response.data));
        })
        .catch((error) => errorHandler(error));
};

export const getUserProfile = () => {
    axios
        .get(USER_PROFILE)
        .then((response) => {
            store.dispatch(setUser(response.data));
            if (response.data.teacher_name) {
                store.dispatch(
                    setTeacher({
                        id: 15,
                        name: response.data.teacher_name,
                        surname: response.data.teacher_surname,
                        patronymic: response.data.teacher_patronymic,
                        position: response.data.teacher_position,
                    }),
                );
            }
        })
        .catch((error) => errorHandler(error));
};

export const updateUserPassword = (values) => {
    axios
        .put(UPDATE_USER_PROFILE, {
            current_password: values.current_password,
            new_password: values.new_password,
        })
        .then((response) => {
            store.dispatch(setUser(response.data));
            successHandler(
                i18n.t(BACK_END_SUCCESS_OPERATION, {
                    cardType: i18n.t(FORM_TEACHER_LABEL),
                    actionType: i18n.t('serviceMessages:updated'),
                }),
            );
        })
        .catch((error) => errorHandler(error));
};

export const updateUserTeacher = (values) => {
    axios
        .put(UPDATE_USER_PROFILE, {
            teacher_name: values.name,
            teacher_surname: values.surname,
            teacher_patronymic: values.patronymic,
            teacher_position: values.position,
        })
        .then((response) => {
            store.dispatch(setUser(response.data));
            store.dispatch(
                setTeacher({
                    id: 15,
                    name: response.data.teacher_name,
                    surname: response.data.teacher_surname,
                    patronymic: response.data.teacher_patronymic,
                    position: response.data.teacher_position,
                }),
            );
            successHandler(
                i18n.t(BACK_END_SUCCESS_OPERATION, {
                    cardType: i18n.t(FORM_USER_LABEL),
                    actionType: i18n.t('serviceMessages:updated'),
                }),
            );
        })
        .catch((error) => errorHandler(error));
};

export const mergeUserAndTeacherService = (mergeObj) => {
    axios
        .put(MERGE_USER_AND_TEACHER_URL, mergeObj)
        .then(() => {
            getTeachersWithoutAccount(); // replace in future to saga from teachers
            getUsersService();
            setLoadingService(false);
            successHandler(
                i18n.t(BACK_END_SUCCESS_OPERATION, {
                    cardType: i18n.t(FORM_USER_LABEL),
                    actionType: i18n.t(SUCCESSFULLY_MERGED),
                }),
            );
        })
        .catch((error) => {
            setLoadingService(false);
            errorHandler(error);
        });
};
