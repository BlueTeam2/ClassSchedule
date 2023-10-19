import { store } from '../store';

import axios from '../helper/axios';
import { GROUP_URL } from '../constants/axios';
import { GROUP_FORM } from '../constants/reduxForms';
import { showAllGroupsSuccess, selectGroupSuccess, updateGroupSuccess } from '../actions/index';
import { errorHandler, successHandler } from '../helper/handlerAxios';
import i18n from '../i18n';
import { resetFormHandler } from '../helper/formHelper';
import {
    BACK_END_SUCCESS_OPERATION,
    UPDATED_LABEL,
} from '../constants/translationLabels/serviceMessages';
import { FORM_GROUP_LABEL } from '../constants/translationLabels/formElements';

export const showAllGroupsService = () => {
    axios
        .get(GROUP_URL)
        .then((response) => {
            store.dispatch(showAllGroupsSuccess(response.data));
        })
        .catch((error) => errorHandler(error));
};

export const selectGroupService = (groupId) => {
    store.dispatch(selectGroupSuccess(groupId));
};

export const updateGroupService = (data) => {
    return axios
        .put(GROUP_URL, data)
        .then((response) => {
            store.dispatch(updateGroupSuccess(response.data));
            selectGroupService(null);
            // getDisabledGroupsService();
            // showAllGroupsService();
            resetFormHandler(GROUP_FORM);
            successHandler(
                i18n.t(BACK_END_SUCCESS_OPERATION, {
                    cardType: i18n.t(FORM_GROUP_LABEL),
                    actionType: i18n.t(UPDATED_LABEL),
                }),
            );
        })
        .catch((error) => errorHandler(error));
};
