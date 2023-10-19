import React from 'react';
import { mount } from 'enzyme';
import { GroupTitle } from './GroupTitle';
import i18n from '../../../i18n';

const props = {
    lessonArray: [
        {
            subject_for_site: 'Web-дизайн',
            teacher_for_site: 'teacher',
            groups: [
                {
                    group_name: '152',
                },
            ],
        },
    ],
    t: i18n.t,
};

describe('GroupTitle', () => {
    it('renders GroupTitle with props', () => {
        const wrapper = mount(<GroupTitle {...props} />);
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.find('.group-list')).toHaveLength(1);
        wrapper.unmount();
    });
});
