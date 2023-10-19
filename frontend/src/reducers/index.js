import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';

import teachers from './teachers';
import lesson from './lesson';
import temporarySchedule from './temporarySchedule';
import snackbar from './snackbar';
import auth from './auth';
import loadingIndicator from './loadingIndicator';
import schedule from './schedule';
import classActions from './class';
import subjects from './subjects';
import groups from './groups';
import rooms from './rooms';
import semesters from './semesters';
import users from './users';
import departments from './departments';
import students from './students';
import dialog from './dialog';

const rootReducer = combineReducers({
    lesson,
    temporarySchedule,
    auth,
    snackbar,
    schedule,
    users,
    loadingIndicator,
    classActions,
    teachers,
    subjects,
    groups,
    rooms,
    semesters,
    departments,
    students,
    dialog,

    form: formReducer,
});

export default rootReducer;
