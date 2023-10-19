import React from 'react';
import { mount, shallow } from 'enzyme';
import { CircularProgress } from '@material-ui/core';
import Lessons from './Lessons';

const onClickOpen = jest.fn();
const onSelectLesson = jest.fn();
const onCopyLesson = jest.fn();

const props = {
    loading: false,
    groupId: 12,
    onClickOpen,
    onSelectLesson,
    onCopyLesson,
    group: {
        id: 4,
        title: '123',
    },

    visibleItems: [
        {
            id: 6256,
            hours: 2,
            linkToMeeting: null,
            subjectForSite: 'Web-технології та Web-програмування',
            lessonType: 'LABORATORY',
            subject: {
                id: 47,
                name: 'Web-технології та Web-програмування',
                disable: false,
            },
            teacher: {
                id: 69,
                name: 'Степан',
                surname: 'Блажевський',
                patronymic: 'Григорович Григорович',
                position: 'доцент',
                email: null,
                department: null,
            },
            semesterId: null,
            group: {
                id: 4,
                title: '123',
            },
            grouped: false,
        },
    ],
};

describe('behavior of Lessons Component', () => {
    it('should render loading if "loading:true"', () => {
        const wrapper = mount(<Lessons {...props} loading />);
        expect(wrapper.find(CircularProgress)).toHaveLength(1);
        wrapper.unmount();
    });

    it('should render h2 text if visible items are empty', () => {
        const wrapper = mount(<Lessons {...props} loading={false} visibleItems={[]} />);
        expect(wrapper.find('h2')).toHaveLength(1);
    });

    it('should render LessonsList component if visible items are not empty', () => {
        const wrapper = shallow(<Lessons {...props} loading={false} />);
        expect(wrapper.find('LessonsList')).toHaveLength(1);
    });
});
