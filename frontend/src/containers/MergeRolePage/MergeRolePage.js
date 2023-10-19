import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { useTranslation } from 'react-i18next';

import TextField from '@material-ui/core/TextField';
import { makeStyles } from '@material-ui/core/styles';
import Autocomplete from '@material-ui/lab/Autocomplete';
import Button from '@material-ui/core/Button';
import { CircularProgress } from '@material-ui/core';
import { setLoadingService } from '../../services/loadingService';
import { getUsersService, mergeUserAndTeacherService } from '../../services/userService';

import Card from '../../share/Card/Card';

import './MergeRolePage.scss';
import {
    FORM_USER_LABEL,
    FORM_TEACHER_LABEL,
    FORM_MERGE_BUTTON,
} from '../../constants/translationLabels/formElements';
import { MERGE_HEADER } from '../../constants/translationLabels/common';
import { getTeacherWithoutAccountStart } from '../../actions/teachers';

const useStyles = makeStyles(() => ({
    autoCompleteField: {
        '&': {
            display: 'inline-block',
            margin: '0 10px 10px 0',
            width: 200,
        },
    },
}));

const MergeRolePage = (props) => {
    const { t } = useTranslation('common');

    const [teacher, setTeacher] = useState(null);
    const [user, setUser] = useState(null);

    const classes = useStyles();

    const { teachers, getTeachersWithoutAccount } = props;
    const { users } = props;

    useEffect(() => {
        getTeachersWithoutAccount();
    }, []);
    useEffect(() => {
        getUsersService();
    }, []);

    const defaultPropsTeachers = {
        options: teachers,
        getOptionLabel: (option) =>
            option
                ? `${option.position} ${option.surname} ${option.name} ${option.patronymic}`
                : '',
    };

    const defaultPropsUsers = {
        options: users,
        getOptionLabel: (option) => (option ? option.email : ''),
    };

    const mergeUserAndTeacherHandle = () => {
        if (!user || !teacher) return;
        mergeUserAndTeacherService({ teacherId: teacher.id, userId: user.id });
        setUser(null);
        setTeacher(null);
        setLoadingService(true);
    };

    return (
        <>
            <div className="merge-role-form">
                <Card additionClassName="merge-role-card">
                    <h2 className="under-line">{t(MERGE_HEADER)}</h2>
                    {props.loading ? (
                        <CircularProgress />
                    ) : (
                        <>
                            <div className="autocomplete-group">
                                <Autocomplete
                                    {...defaultPropsTeachers}
                                    clearOnEscape
                                    openOnFocus
                                    className={classes.autoCompleteField}
                                    onChange={(event, newValue) => {
                                        setTeacher(newValue);
                                    }}
                                    renderInput={(params) => (
                                        <TextField
                                            {...params}
                                            label={t(FORM_TEACHER_LABEL)}
                                            margin="normal"
                                        />
                                    )}
                                />
                                <Autocomplete
                                    {...defaultPropsUsers}
                                    clearOnEscape
                                    openOnFocus
                                    className={classes.autoCompleteField}
                                    onChange={(event, newValue) => {
                                        setUser(newValue);
                                    }}
                                    renderInput={(params) => (
                                        <TextField
                                            {...params}
                                            label={t(FORM_USER_LABEL)}
                                            margin="normal"
                                        />
                                    )}
                                />
                            </div>
                            <Button
                                className="merge-btn"
                                variant="contained"
                                color="primary"
                                onClick={() => mergeUserAndTeacherHandle()}
                            >
                                {t(FORM_MERGE_BUTTON)}
                            </Button>
                        </>
                    )}
                </Card>
            </div>
        </>
    );
};
const mapStateToProps = (state) => ({
    teachers: state.teachers.teachers,
    users: state.users.users,
    loading: state.loadingIndicator.loading,
});

const mapDispatchToProps = (dispatch) => ({
    getTeachersWithoutAccount: () => dispatch(getTeacherWithoutAccountStart()),
});

export default connect(mapStateToProps, mapDispatchToProps)(MergeRolePage);
