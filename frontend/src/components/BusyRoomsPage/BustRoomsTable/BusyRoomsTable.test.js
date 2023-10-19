import React from 'react';
import { mount } from 'enzyme';
import BusyRoomsTable from './BusyRoomsTable';
import i18n from '../../../i18n';

const props = {
    days: ['MONDAY', 'THURSDAY', 'FRIDAY'],
    columnsSize: 'sm',
    classes: [
        {
            id: 1,
            startTime: '08:19',
            endTime: '09:39',
            class_name: '1',
        },
    ],
    busyRooms: [
        {
            schedules: [
                {
                    day: 'MONDAY',
                    classes: [
                        {
                            even: [
                                {
                                    lessons: [
                                        {
                                            groups: [
                                                {
                                                    group_id: 31,
                                                    group_name: '18 (106)',
                                                },
                                            ],
                                            subject_for_site: 'Аналітична геометрія',
                                            lesson_type: 'LECTURE',
                                            teacher_for_site: 'Мироник',
                                        },
                                        {
                                            groups: [
                                                {
                                                    group_id: 28,
                                                    group_name: '19 (105)',
                                                },
                                            ],
                                            subject_for_site:
                                                'Актуальні питання історії та культури України',
                                            lesson_type: 'PRACTICAL',
                                            teacher_for_site: 'Дробіна',
                                        },
                                    ],
                                    class_id: 1,
                                    class_name: '1',
                                },
                                {
                                    lessons: [],
                                    class_id: 2,
                                    class_name: '2',
                                },
                            ],
                            odd: [
                                {
                                    lessons: [],
                                    class_id: 1,
                                    class_name: '1',
                                },
                                {
                                    lessons: [],
                                    class_id: 2,
                                    class_name: '2',
                                },
                            ],
                        },
                    ],
                },
            ],
            room_id: 51,
            room_name: '1 к.  11 ауд',
            room_type: 'практична',
        },
    ],
    t: i18n.t,
};

describe('<BusyRoomsTable />', () => {
    it('should renders BusyRoomsTable with props', () => {
        const wrapper = mount(<BusyRoomsTable {...props} />);
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.find('.view-rooms')).toHaveLength(1);
        wrapper.unmount();
    });
    it('should render correct room name and title', () => {
        const wrapper = mount(<BusyRoomsTable {...props} />);
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.find('.busy-room-title').prop('title')).toEqual('практична');
        expect(wrapper.find('.busy-room-title').prop('children')).toEqual('1 к.  11 ауд');
        wrapper.unmount();
    });
    describe('render table item', () => {
        it('should render table item inside table', () => {
            const wrapper = mount(<BusyRoomsTable {...props} />);
            expect(wrapper.exists()).toBeTruthy();
            expect(wrapper.find('TableItem')).toHaveLength(1);
            wrapper.unmount();
        });
        it('should render table without item inside table', () => {
            props.days.shift();
            const wrapper = mount(<BusyRoomsTable {...props} />);
            expect(wrapper.exists()).toBeTruthy();
            expect(wrapper.find('TableItem')).toHaveLength(0);
            wrapper.unmount();
        });
    });
    it('should render empty view-rooms div', () => {
        props.busyRooms = [];
        const wrapper = mount(<BusyRoomsTable {...props} />);
        expect(wrapper.exists()).toBeTruthy();
        expect(wrapper.find('.view-rooms').prop('children')).toEqual([]);
        wrapper.unmount();
    });
});
