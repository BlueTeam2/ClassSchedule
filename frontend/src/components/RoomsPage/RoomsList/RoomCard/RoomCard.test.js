import React from 'react';
import { mount } from 'enzyme';
import RoomCard from './RoomCard';

const showConfirmDialog = jest.fn();
const setSelectRoom = jest.fn();
const props = {
    room: {
        id: 73,
        name: 'Пара проводиться онлайн',
        type: {
            id: 25,
            description: 'Лекційна',
        },
    },
    showConfirmDialog,
    setSelectRoom,
    isDisabled: true,
};

describe('behavior of RoomCard Component when room is disabled', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(<RoomCard {...props} />);
    });

    afterEach(() => wrapper.unmount());

    it('should render GiSightDisabled and MdDelete icons if disabled is true', () => {
        expect(wrapper.find('GiSightDisabled')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
    });
    it('should call showConfirmDialog when click to GiSightDisabled icon', () => {
        wrapper.find('GiSightDisabled').simulate('click');
        expect(props.showConfirmDialog.mock.calls.length).toEqual(1);
    });
    it('should call showConfirmDialog when click to MdDelete icon', () => {
        wrapper.find('MdDelete').simulate('click');
        expect(props.showConfirmDialog.mock.calls.length).toEqual(1);
        expect(props.showConfirmDialog).toBeCalledWith(props.room.id, 'deleteConfirm', 'room');
    });
});

describe('behavior of RoomCard Component when room is not disabled', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(<RoomCard {...props} isDisabled={false} />);
    });

    afterEach(() => wrapper.unmount());

    it('should render IoMdEye, FaEdit and MdDelete icons if disabled is false', () => {
        expect(wrapper.find('IoMdEye')).toHaveLength(1);
        expect(wrapper.find('FaEdit')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
    });
    it('should call showConfirmDialog when click to IoMdEye icon', () => {
        wrapper.find('IoMdEye').simulate('click');
        expect(props.showConfirmDialog.mock.calls.length).toEqual(1);
    });
    it('should call setSelectRoom when click to FaEdit icon', () => {
        wrapper.find('FaEdit').simulate('click');
        expect(props.setSelectRoom.mock.calls.length).toEqual(1);
    });
});
