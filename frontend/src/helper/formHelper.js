import { reset } from 'redux-form';
import { store } from '../store';

export const resetFormHandler = (formName) => {
    store.dispatch(reset(formName));
};
