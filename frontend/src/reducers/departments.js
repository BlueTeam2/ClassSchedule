import * as actionTypes from '../actions/actionsType';

const initialState = {
    departments: [],
    department: {},
    disabledDepartments: [],
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.ADD_DEPARTMENT:
            return { ...state, departments: state.departments.concat(action.result) };
        case actionTypes.GET_ALL_DEPARTMENTS: {
            const departments = action.result.filter((department) => department.disable === false);
            const disabledDepartments = action.result.filter(
                (department) => department.disable === true,
            );
            return { ...state, departments, disabledDepartments };
        }
        case actionTypes.CLEAR_DEPARTMENT_FORM:
            return {
                ...state,
                department: {},
            };
        case actionTypes.DELETE_DEPARTMENT: {
            const departments = state.departments.filter(
                (department) => department.id !== action.result.id,
            );
            const disabledDepartments = state.disabledDepartments.filter(
                (department) => department.id !== action.result.id,
            );
            return { ...state, departments, disabledDepartments };
        }
        case actionTypes.GET_DISABLED_DEPARTMENTS:
            return { ...state, disabledDepartments: action.result };
        case actionTypes.SET_DISABLED_DEPARTMENT: {
            const departments = state.departments.filter(
                (department) => department.id !== action.result.id,
            );
            const disabledDepartments = [...state.disabledDepartments, action.result];
            return { ...state, departments, disabledDepartments };
        }
        case actionTypes.SET_ENABLED_DEPARTMENT: {
            const disabledDepartments = state.disabledDepartments.filter(
                (department) => department.id !== action.result.id,
            );
            const departments = [...state.departments, action.result];
            return { ...state, departments, disabledDepartments };
        }
        case actionTypes.GET_DEPARTMENT_BY_ID: {
            let getDepartment = state.departments.find(
                (department) => department.id === action.result,
            );
            if (!getDepartment) {
                getDepartment = { id: null };
            }
            return { ...state, department: getDepartment };
        }
        case actionTypes.UPDATE_DEPARTMENT: {
            const departmentIndex = state.departments.findIndex((department) => {
                return department.id === action.result.id;
            });
            const departments = [...state.departments];
            departments[departmentIndex] = {
                ...departments[departmentIndex],
                ...action.result,
            };

            const disDepartmentIndex = state.disabledDepartments.findIndex((disabledDept) => {
                return disabledDept.id === action.result;
            });
            const disabledDepartments = [...state.departments];
            disabledDepartments[disDepartmentIndex] = {
                ...disabledDepartments[disDepartmentIndex],
                ...action.result,
            };
            return { ...state, departments, disabledDepartments, department: {} };
        }

        default:
            return state;
    }
};

export default reducer;
