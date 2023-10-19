import React from 'react';
import { useTranslation } from 'react-i18next';
import Button from '@material-ui/core/Button';
import { MdPlayArrow } from 'react-icons/md';
import './SemesterCopyForm.scss';
import {
    FORM_SEMESTER_COPY_RED_HINT,
    FORM_SEMESTER_COPY_HINT,
} from '../../../../constants/translationLabels/formElements';
import { TEACHER_SCHEDULE_LABEL } from '../../../../constants/translationLabels/common';
import SemesterSelect from './SemesterSelect';

const SemesterCopyForm = (props) => {
    const { t } = useTranslation('common');
    const { semesterId, semesters, handleSubmit, pristine, submitting, submitButtonLabel } = props;

    return (
        <section>
            <p>{t(FORM_SEMESTER_COPY_HINT)}</p>
            <p className="semester-copy-title">{t(FORM_SEMESTER_COPY_RED_HINT)}</p>
            <form onSubmit={handleSubmit} className="semester-copy-form">
                <SemesterSelect semesterId={semesterId} semesters={semesters} />
                <Button
                    variant="contained"
                    color="primary"
                    type="submit"
                    disabled={pristine || submitting}
                >
                    <MdPlayArrow title={t(TEACHER_SCHEDULE_LABEL)} className="svg-btn" />
                    {submitButtonLabel}
                </Button>
            </form>
        </section>
    );
};

export default SemesterCopyForm;
