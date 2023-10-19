import React from 'react';
import { shallow } from 'enzyme';

import LessonsList from './LessonsList';

const props = {
    lessons: [
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
                id: 92,
                title: '123',
            },
            grouped: false,
        },
    ],
};

describe('behavior of LessonsList Component', () => {
    it('should render LessonsCard component if lessons exists', () => {
        const wrapper = shallow(<LessonsList {...props} />);
        expect(wrapper.find('LessonsCard')).toHaveLength(1);
    });
});
