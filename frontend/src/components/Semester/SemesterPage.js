import React, { useEffect, useState } from 'react';
import SemesterList from '../../containers/SemesterPage/SemesterList';
import { getGroupsOptionsForSelect } from '../../utils/selectUtils';
import SemesterSidebar from './SemesterSidebar';

const SemesterPage = (props) => {
    const {
        groups,
        semester,
        getAllSemestersItems,
        getDisabledSemestersItems,
        handleSemesterFormSubmit,
        setOpenErrorSnackbar,
        getAllGroupsItems,
        classScheduler,
        // it doesnt work, need to finish implement archived functionality
        // getArchivedSemestersItems,
        // archivedSemesters,
    } = props;

    const [term, setTerm] = useState('');
    const [disabled, setDisabled] = useState(false);
    const [archived, setArchived] = useState(false);

    const options = getGroupsOptionsForSelect(groups);
    useEffect(() => {
        getAllGroupsItems();
    }, []);

    useEffect(() => {
        if (disabled) {
            getDisabledSemestersItems();
        } else {
            getAllSemestersItems();
        }
        // it doesnt work, need to finish implement archived functionality
        // getArchivedSemestersItems();
    }, [disabled]);
    // it doesnt work, need to finish implement archeved functionality
    // const showArchivedHandler = () => {
    //     setArchived(!archived);
    //     setDisabled(false);
    //     return !archived ? setScheduleTypeService('archived') : setScheduleTypeService('default');
    // };

    return (
        <div className="cards-container">
            <SemesterSidebar
                setTerm={setTerm}
                archived={archived}
                disabled={disabled}
                showDisabledHandle={() => {
                    setDisabled(!disabled);
                    setArchived(false);
                }}
                setOpenErrorSnackbar={setOpenErrorSnackbar}
                handleSemesterFormSubmit={handleSemesterFormSubmit}
                semester={semester}
                options={options}
                classScheduler={classScheduler}
            />
            <SemesterList term={term} archived={archived} disabled={disabled} options={options} />
        </div>
    );
};

export default SemesterPage;
