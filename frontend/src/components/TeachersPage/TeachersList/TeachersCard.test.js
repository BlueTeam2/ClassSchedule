import React from 'react';
import { shallow } from 'enzyme';
import TeachersCard from './TeachersCard';
import { dialogTypes } from '../../../constants/dialogs';
import { TEACHER_DEPARTMENT } from '../../../constants/translationLabels/common';

const showConfirmDialog = jest.fn();
const selectedTeacherCard = jest.fn();
const props = {
    teacherItem: {
        id: 1,
        name: 'name',
        surname: 'surname',
        patronymic: 'patronymic',
        position: 'position',
        department: { name: 'departmentName' },
        email: 'test@gmail.com',
    },
    isDisabled: true,
    showConfirmDialog,
    selectedTeacherCard,
};

const props2 = {
    teacherItem: {
        id: 1,
        name: 'name',
        surname: 'surname',
        patronymic: 'patronymic',
        position: 'position',
        department: null,
        email: null,
    },
    isDisabled: true,
    showConfirmDialog,
    selectedTeacherCard,
};

describe('behaviour TeachersCard when teacher is disabled', () => {
    let wrapper;
    beforeEach(() => {
        wrapper = shallow(<TeachersCard {...props} />);
    });

    it('should render GiSightDisabled and MdDelete icons if disabled is true', () => {
        expect(wrapper.find('GiSightDisabled')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
    });

    it('should call showConfirmDialog when click to GiSightDisabled icon', () => {
        wrapper.find('GiSightDisabled').simulate('click');

        expect(showConfirmDialog).toHaveBeenCalled();
        expect(showConfirmDialog).toHaveBeenCalledWith(
            props.teacherItem.id,
            dialogTypes.SET_VISIBILITY_ENABLED,
        );
    });

    it('should call showConfirmDialog when click to MdDelete icon', () => {
        wrapper.find('MdDelete').simulate('click');

        expect(showConfirmDialog).toHaveBeenCalled();
        expect(showConfirmDialog).toHaveBeenCalledWith(
            props.teacherItem.id,
            dialogTypes.DELETE_CONFIRM,
        );
    });
});

describe('behaviour TeachersCard when teacher is not disabled', () => {
    let wrapper;
    beforeEach(() => {
        wrapper = shallow(<TeachersCard {...props} isDisabled={false} />);
    });

    it('should render IoMdEye, FaEdit and MdDelete icons if disabled is false', () => {
        expect(wrapper.find('IoMdEye')).toHaveLength(1);
        expect(wrapper.find('FaEdit')).toHaveLength(1);
        expect(wrapper.find('MdDelete')).toHaveLength(1);
    });

    it('should call showConfirmDialog when click to IoMdEye icon', () => {
        wrapper.find('IoMdEye').simulate('click');

        expect(showConfirmDialog).toHaveBeenCalled();
        expect(showConfirmDialog).toHaveBeenCalledWith(
            props.teacherItem.id,
            dialogTypes.SET_VISIBILITY_DISABLED,
        );
    });

    it('should call selectedTeacherCard when click to FaEdit icon', () => {
        wrapper.find('FaEdit').simulate('click');

        expect(selectedTeacherCard).toHaveBeenCalled();
        expect(selectedTeacherCard).toHaveBeenCalledWith(props.teacherItem.id);
    });

    it('should have correct teacher full name', () => {
        const { surname, name, patronymic } = props.teacherItem;
        const teacherFullName = wrapper.find('.teacher-card__name').text();

        expect(teacherFullName).toEqual(`${surname} ${name} ${patronymic}`);
    });

    it('should have correct teacher title', () => {
        const { position, department } = props.teacherItem;
        const teacherTitle = wrapper.find('.teacher-card__title').text();

        expect(teacherTitle).toEqual(`${position} ${TEACHER_DEPARTMENT} ${department.name}`);
    });

    it('should render email if email passed', () => {
        const { email } = props.teacherItem;
        const teacherEmail = wrapper.find('.teacher-card__email').text();

        expect(teacherEmail).toEqual(email);
    });
});

describe('behaviour card if teacher no department or email', () => {
    let wrapper;
    beforeEach(() => {
        wrapper = shallow(<TeachersCard {...props2} />);
    });

    it('should not render email', () => {
        expect(wrapper.find('.teacher-card__email')).toHaveLength(0);
    });

    it('should have only position in title if department is null', () => {
        const { position } = props.teacherItem;
        const teacherTitle = wrapper.find('.teacher-card__title').text();

        expect(teacherTitle).toEqual(`${position} `);
    });
});
