import i18n from '../i18n';
import { createMessage, createErrorMessage, createDynamicMessage } from './sagaUtils';
import {
    FORM_GROUP_LABEL,
    FORM_SEMESTER_LABEL,
    FORM_STUDENT_LABEL,
} from '../constants/translationLabels/formElements';
import { GROUP } from '../constants/scheduleTypes';
import {
    BACK_END_SUCCESS_OPERATION,
    UPDATED_LABEL,
    CREATED_LABEL,
    DELETED_LABEL,
} from '../constants/translationLabels/serviceMessages';

describe('createErrorMessage function', () => {
    it('should return data.massage', () => {
        const response = { data: { message: 'Unable to delete group' } };
        expect(createErrorMessage({ response })).toEqual('Unable to delete group');
    });
    it('should return message Error', () => {
        const response = undefined;
        expect(createErrorMessage({ response })).toEqual('Error');
    });
});

describe('createMessage function', () => {
    it('should return correct message', () => {
        expect(
            createMessage(BACK_END_SUCCESS_OPERATION, FORM_SEMESTER_LABEL, UPDATED_LABEL),
        ).toEqual(
            i18n.t(BACK_END_SUCCESS_OPERATION, {
                cardType: i18n.t(FORM_SEMESTER_LABEL),
                actionType: i18n.t(UPDATED_LABEL),
            }),
        );
    });
});

const messageForGroup = (message) => {
    return i18n.t(BACK_END_SUCCESS_OPERATION, {
        cardType: i18n.t(FORM_GROUP_LABEL),
        actionType: i18n.t(message),
    });
};

const messageForStudent = (message) => {
    return i18n.t(BACK_END_SUCCESS_OPERATION, {
        cardType: i18n.t(FORM_STUDENT_LABEL),
        actionType: i18n.t(message),
    });
};

describe('createDynamicMessage function for group', () => {
    it('should return create group message', () => {
        expect(createDynamicMessage(GROUP, CREATED_LABEL)).toEqual(messageForGroup(CREATED_LABEL));
    });
    it('should return update group message', () => {
        expect(createDynamicMessage(GROUP, UPDATED_LABEL)).toEqual(messageForGroup(UPDATED_LABEL));
    });
    it('should return delete group message', () => {
        expect(createDynamicMessage(GROUP, DELETED_LABEL)).toEqual(messageForGroup(DELETED_LABEL));
    });
});

describe('createDynamicMessage function for student', () => {
    it('should return create student message', () => {
        expect(createDynamicMessage(GROUP, CREATED_LABEL)).toEqual(
            messageForStudent(CREATED_LABEL),
        );
    });
    it('should return update student message', () => {
        expect(createDynamicMessage(GROUP, UPDATED_LABEL)).toEqual(
            messageForStudent(UPDATED_LABEL),
        );
    });
    it('should return delete student message', () => {
        expect(createDynamicMessage(GROUP, DELETED_LABEL)).toEqual(
            messageForStudent(DELETED_LABEL),
        );
    });
});
