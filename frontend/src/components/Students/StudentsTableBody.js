import React from 'react';
import './StudentTable.scss';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableRow from '@material-ui/core/TableRow';
import Checkbox from '@material-ui/core/Checkbox';
import { useTranslation } from 'react-i18next';
import { FaEdit } from 'react-icons/all';
import { Delete } from '@material-ui/icons';
import { Link } from 'react-router-dom';
import { getTeacherFullName } from '../../helper/renderTeacher';
import {
    STUDENT_LINK,
    EDIT_LINK,
    DELETE_LINK,
    GROUP_LIST_LINK,
    SHOW_STUDENTS_LINK,
} from '../../constants/links';
import {
    EDIT_TITLE,
    SEND_LETTER_LABEL,
    SELECT_STUDENT,
    DELETE_TITLE_LABEL,
} from '../../constants/translationLabels/formElements';

export const StudentsTableBody = (props) => {
    const {
        group,
        setStudent,
        checkStudent,
        setIsOpenUpdateDialog,
        selectStudentSuccess,
        currentStudentsOnList,
        setIsOpenConfirmDialog,
    } = props;
    const { t } = useTranslation('formElements');

    const sendMail = (email) => {
        const mailto = `mailto:${email}`;
        window.location.href = mailto;
    };

    return (
        <TableBody>
            {currentStudentsOnList.map((student) => (
                <TableRow key={student.id}>
                    <TableCell component="th" scope="row" align="center">
                        <div className="checkbox-table-body">
                            <Checkbox
                                onChange={(e) => checkStudent(e)}
                                checked={student.checked}
                                value={student.id}
                                color="primary"
                                size="small"
                                title={`${t(SELECT_STUDENT)} ${getTeacherFullName(student)}`}
                            />
                        </div>
                    </TableCell>
                    <TableCell component="th" scope="row" align="center">
                        {getTeacherFullName(student)}
                    </TableCell>
                    <TableCell component="th" scope="row" align="center">
                        <button
                            className="send-letter-button"
                            title={`${t(SEND_LETTER_LABEL)} ${student.email}`}
                            onClick={() => sendMail(student.email)}
                            type="button"
                        >
                            {student.email}
                        </button>
                    </TableCell>
                    <TableCell component="th" scope="row" align="center">
                        <div className="edit-cell-table">
                            <Link
                                to={`${GROUP_LIST_LINK}/${group.id}${SHOW_STUDENTS_LINK}/${student.id}${EDIT_LINK}`}
                            >
                                <FaEdit
                                    className="edit-button-table"
                                    title={t(EDIT_TITLE)}
                                    onClick={() => {
                                        selectStudentSuccess(student.id);
                                        setIsOpenUpdateDialog(true);
                                    }}
                                />
                            </Link>
                            <Link
                                to={`${GROUP_LIST_LINK}/${group.id}${STUDENT_LINK}/${student.id}${DELETE_LINK}`}
                            >
                                <Delete
                                    title={t(DELETE_TITLE_LABEL)}
                                    className="delete-button-table"
                                    onClick={() => {
                                        setStudent(student);
                                        setIsOpenConfirmDialog(true);
                                    }}
                                />
                            </Link>
                        </div>
                    </TableCell>
                </TableRow>
            ))}
        </TableBody>
    );
};
