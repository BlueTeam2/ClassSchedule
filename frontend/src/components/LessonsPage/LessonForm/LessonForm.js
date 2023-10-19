import React, { useState, useEffect } from 'react';
import { Field } from 'redux-form';
import { useTranslation } from 'react-i18next';

import Button from '@material-ui/core/Button';
import Accordion from '@material-ui/core/Accordion';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Typography from '@material-ui/core/Typography';

import Card from '../../../share/Card/Card';
import renderTextField from '../../../share/renderedFields/input';
import renderCheckboxField from '../../../share/renderedFields/checkbox';

import { isUrl, lessThanZero, maxLengthValue, required } from '../../../validation/validateFields';
import { handleTeacherInfo } from '../../../helper/renderTeacher';
import { getClearOrCancelTitle, setDisableButton } from '../../../helper/disableComponent';
import { RenderMultiselect } from '../../../share/renderedFields/renderMultiselect';
import { renderAutocompleteField } from '../../../helper/renderAutocompleteField';
import LessonLabelForm from '../../../containers/LessonPage/LessonLabelForm';

import {
    SAVE_BUTTON_LABEL,
    GROUP_LABEL,
    SUBJECT_LABEL,
    NOT_SELECTED_LABEL,
    GROUPS_LABEL,
    COPY_GROUPS_LABEL,
    COPY_FOR_BUTTON_LABEL,
    LINK_TO_MEETING_LABEL,
    FOR_SITE_LABEL,
    FORM_GROUPED_LABEL,
    TEACHER_LABEL,
    TYPE_LABEL,
    HOURS_LABEL_SHORT,
} from '../../../constants/translationLabels/formElements';

import './LessonForm.scss';
import '../LessonPage.scss';

const LessonForm = (props) => {
    const { t } = useTranslation('formElements');

    const {
        handleSubmit,
        pristine,
        reset,
        submitting,
        groups,
        group,
        lesson,
        isUniqueError,
        teachers,
        subjects,
        groupId,
        initialize,
        change,
        lessonTypes,
        selectLessonCardSuccess,
        setUniqueError,
        selectGroupSuccess,
    } = props;
    const [checked, setChecked] = useState(false);

    const lessonId = lesson.id;

    const initializeFormHandler = (lessonData) => {
        const {
            id: lessonCardId,
            teacher,
            subject,
            lessonType: type,
            hours,
            linkToMeeting,
            subjectForSite,
            grouped,
        } = lessonData;

        initialize({
            lessonCardId,
            teacher,
            subject,
            type,
            hours,
            linkToMeeting,
            subjectForSite,
            grouped,
            groups: [group],
        });

        setChecked(lessonData.grouped);
    };
    const handleChange = (event) => setChecked(event.target.checked);

    useEffect(() => {
        selectGroupSuccess(groupId);
    }, [groupId]);

    useEffect(() => {
        setChecked(false);
        if (lessonId) {
            initializeFormHandler(lesson);
        } else {
            initialize({ groups: [group] });
        }
    }, [lessonId, group]);

    const valid = !isUniqueError ? { validate: [required] } : { error: isUniqueError };

    const clearForm = () => {
        reset();
        setUniqueError(null);
        selectLessonCardSuccess(null);
    };

    return (
        <>
            {groupId ? (
                <Card additionClassName="lesson-form-card">
                    <LessonLabelForm />
                    <form onSubmit={handleSubmit} className="lesson-form">
                        <Field
                            name="teacher"
                            component={renderAutocompleteField}
                            {...valid}
                            label={t(TEACHER_LABEL)}
                            type="text"
                            getItemTitle={handleTeacherInfo}
                            values={teachers}
                            onChange={() => setUniqueError(false)}
                            getOptionLabel={(option) => (option ? handleTeacherInfo(option) : '')}
                        />
                        <Field
                            name="subject"
                            component={renderAutocompleteField}
                            {...valid}
                            label={t(SUBJECT_LABEL)}
                            type="text"
                            getItemTitle={(sub) => sub.name}
                            values={subjects}
                            onChange={(subject) => {
                                change('subjectForSite', subject?.name ?? '');
                                setUniqueError(false);
                            }}
                            getOptionLabel={(option) =>
                                subjects.find((subject) => subject.id === option.id)?.name ?? ''
                            }
                        />
                        <div className="form-fields-container">
                            <Field
                                name="type"
                                component={renderAutocompleteField}
                                {...valid}
                                label={t(TYPE_LABEL)}
                                type="text"
                                getItemTitle={(type) => {
                                    t(`formElements:lesson_type_${type.toLowerCase()}_label`);
                                }}
                                values={lessonTypes}
                                onChange={() => {
                                    setUniqueError(false);
                                }}
                                getOptionLabel={(lessonType) =>
                                    lessonType
                                        ? t(
                                              `formElements:lesson_type_${lessonType.toLowerCase()}_label`,
                                          )
                                        : ''
                                }
                            />

                            <Field
                                id="hours"
                                name="hours"
                                type="tel"
                                component={renderTextField}
                                label={t(HOURS_LABEL_SHORT)}
                                validate={[required, lessThanZero]}
                            />
                            <Field
                                id="grouped"
                                name="grouped"
                                label={t(FORM_GROUPED_LABEL)}
                                labelPlacement="Top"
                                defaultValue={checked}
                                component={renderCheckboxField}
                                checked={checked}
                                onChange={handleChange}
                                color="primary"
                            />
                        </div>
                        <Field
                            id="linkToMeeting"
                            name="linkToMeeting"
                            rowsMax="1"
                            margin="normal"
                            component={renderTextField}
                            label={t(LINK_TO_MEETING_LABEL)}
                            validate={[isUrl, maxLengthValue]}
                            placeholder="Input URL"
                        />
                        <Field
                            id="subjectForSite"
                            name="subjectForSite"
                            multiline
                            rowsMax="1"
                            margin="normal"
                            component={renderTextField}
                            label={t(SUBJECT_LABEL) + t(FOR_SITE_LABEL)}
                            validate={[required, maxLengthValue]}
                        />
                        {!lessonId && (
                            <Accordion>
                                <AccordionSummary
                                    expandIcon={<ExpandMoreIcon />}
                                    aria-controls="panel1a-content"
                                    id="panel1a-header"
                                >
                                    <Typography>{t(COPY_FOR_BUTTON_LABEL)}</Typography>
                                </AccordionSummary>
                                <AccordionDetails>
                                    <Typography>
                                        <>
                                            <p className="group-label">
                                                <label htmlFor="groups">
                                                    {t(COPY_GROUPS_LABEL)}
                                                </label>
                                            </p>
                                            <Field
                                                id="groups"
                                                name="groups"
                                                component={RenderMultiselect}
                                                options={groups}
                                                value={[group]}
                                                displayValue="title"
                                                className="form-control mt-2"
                                                placeholder={t(GROUPS_LABEL)}
                                                hidePlaceholder
                                                selectedValues={[group]}
                                                alwaysDisplayedItem={group}
                                            />
                                        </>
                                    </Typography>
                                </AccordionDetails>
                            </Accordion>
                        )}
                        <div className="form-buttons-container">
                            <Button
                                className="buttons-style"
                                type="submit"
                                variant="contained"
                                color="primary"
                                disabled={pristine || submitting}
                                onClick={() => {
                                    setChecked(false);
                                }}
                            >
                                {t(SAVE_BUTTON_LABEL)}
                            </Button>
                            <Button
                                className="buttons-style"
                                type="button"
                                variant="contained"
                                disabled={setDisableButton(pristine, submitting, lesson.id)}
                                onClick={() => {
                                    clearForm();
                                }}
                            >
                                {getClearOrCancelTitle(lesson.id, t)}
                            </Button>
                        </div>
                    </form>
                </Card>
            ) : (
                <h2 className="not-selected">{`${t(GROUP_LABEL)} ${t(NOT_SELECTED_LABEL)}`}</h2>
            )}
        </>
    );
};

export default LessonForm;
