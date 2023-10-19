import React from 'react';
import { mount } from 'enzyme';

import TeachersList from './TeachersList';

const props = {
    visibleItems: [
        {
            id: 1,
            name: 'name',
            surname: 'biga123',
            patronymic: 'biga123',
            position: 'асистент',
            email: 'biga12213@mail.com',
            department: {
                id: 2,
                name: 'Programming',
                disable: false,
            },
        },
    ],
};

describe('behavior of TeachersList Component', () => {
    it('should render NotFound component if visible items are empty', () => {
        const wrapper = mount(<TeachersList {...props} visibleItems={[]} />);
        expect(wrapper.find('NotFound')).toHaveLength(1);
        wrapper.unmount();
    });
    it('should render TeachersCard component if visible items are not empty', () => {
        const wrapper = mount(<TeachersList {...props} />);
        expect(wrapper.find('TeachersCard')).toHaveLength(1);
        wrapper.unmount();
    });
});
