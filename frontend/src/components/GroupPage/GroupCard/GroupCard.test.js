import React from 'react';
import { mount } from 'enzyme';
import { BrowserRouter } from 'react-router-dom';
import GroupCard from './GroupCard';

const showAddStudentDialog = jest.fn();
const showStudentsByGroup = jest.fn();
const showConfirmDialog = jest.fn();
const setGroup = jest.fn();
const props = {
    group: {
        id: 116,
        title: '100',
    },
    showAddStudentDialog,
    showStudentsByGroup,
    showConfirmDialog,
    setGroup,
    disabled: true,
};

describe('behavior of GroupCard Component when group is disabled', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(
            <BrowserRouter>
                <GroupCard {...props} />
            </BrowserRouter>,
        );
    });

    afterEach(() => wrapper.unmount());

    it('should render GiSightDisabled and MdDelete icons if disabled is true', () => {
        expect(wrapper.find('GiSightDisabled')).toHaveLength(1);
        expect(wrapper.find('FaUserPlus')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
    });
    it('should call showConfirmDialog when click to GiSightDisabled icon', () => {
        wrapper.find('GiSightDisabled').simulate('click');
        expect(props.showConfirmDialog.mock.calls.length).toEqual(1);
    });
    it('should call showConfirmDialog when click to MdDelete icon', () => {
        wrapper.find('MdDelete').simulate('click');
        expect(props.showConfirmDialog.mock.calls.length).toEqual(1);
    });
    it('should call showAddStudentDialog when click to FaUserPlus icon', () => {
        wrapper.find('FaUserPlus').simulate('click');
        expect(props.showAddStudentDialog.mock.calls.length).toEqual(1);
    });
});

describe('behavior of GroupCard Component when group is not disabled', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = mount(
            <BrowserRouter>
                <GroupCard {...props} disabled={false} />
            </BrowserRouter>,
        );
    });

    afterEach(() => wrapper.unmount());

    it('should render IoMdEye, FaEdit, FaUsers and MdDelete icons if disabled is false', () => {
        expect(wrapper.find('IoMdEye')).toHaveLength(1);
        expect(wrapper.find('FaEdit')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
        expect(wrapper.find('FaUserPlus')).toHaveLength(1);
        expect(wrapper.find('FaUsers')).toHaveLength(1);
    });
    it('should call showConfirmDialog when click to IoMdEye icon', () => {
        wrapper.find('IoMdEye').simulate('click');
        expect(props.showConfirmDialog.mock.calls.length).toEqual(1);
    });
    it('should call setGroup when click to FaEdit icon', () => {
        wrapper.find('FaEdit').simulate('click');
        expect(props.setGroup.mock.calls.length).toEqual(1);
    });
    it('should call showStudentsByGroup when click to FaUsers icon', () => {
        wrapper.find('FaUsers').simulate('click');
        expect(props.showStudentsByGroup.mock.calls.length).toEqual(1);
    });
});
