import React from 'react';
import { mount } from 'enzyme';
import TableItem from './TableItem';

const props = {
    index: '071',
    classes: [
        {
            id: 1,
            startTime: '08:19',
            endTime: '09:39',
            class_name: '1',
        },
    ],
    schedule: {
        day: 'MONDAY',
        classes: [
            {
                even: [
                    {
                        lessons: [
                            {
                                groups: [
                                    {
                                        group_id: 33,
                                        group_name: '17 (108)',
                                    },
                                ],
                                subject_for_site: 'Аналітична геометрія',
                                lesson_type: 'PRACTICAL',
                                teacher_for_site: 'Мироник',
                            },
                        ],
                        class_id: 1,
                        class_name: '1',
                    },
                ],
                odd: [],
            },
        ],
    },
};

describe('<TableItem />', () => {
    it('should render empty TableItem if classes array empty', () => {
        const wrapper = mount(<TableItem classes={[]} />);
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.prop('children')).toEqual(undefined);
        wrapper.unmount();
    });
    it('should render odd and even table item', () => {
        const wrapper = mount(<TableItem {...props} />);
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.find('.class-info-container')).toHaveLength(2);
        wrapper.unmount();
    });
    it('should render even card with group name', () => {
        const wrapper = mount(<TableItem {...props} />);
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.find('.group-list')).toHaveLength(1);
        wrapper.unmount();
    });
});
