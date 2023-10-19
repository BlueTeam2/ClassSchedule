import { store } from '../store';

import { setLoading, setScheduleLoading, setSemesterLoading } from '../actions/index';

export const setLoadingService = (isLoading) => {
    store.dispatch(setLoading(isLoading));
};

export const setScheduleLoadingService = (isLoading) => {
    store.dispatch(setScheduleLoading(isLoading));
};

export const setSemesterLoadingService = (isLoading) => {
    store.dispatch(setSemesterLoading(isLoading));
};
