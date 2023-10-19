import React, { useEffect, useState } from 'react';
import { connect } from 'react-redux';
import { isNil } from 'lodash';
import { Link } from 'react-router-dom';
import {
    FaCaretDown,
    FaClipboardList,
    FaClock,
    FaHome,
    FaRunning,
    FaSignOutAlt,
    FaUser,
} from 'react-icons/fa';
import Menu from '@material-ui/core/Menu';
import Button from '@material-ui/core/Button';
import { useTranslation } from 'react-i18next';
import MenuItem from '@material-ui/core/MenuItem';
import { withStyles } from '@material-ui/core/styles';
import ListItemIcon from '@material-ui/core/ListItemIcon';

import './Header.scss';
import CircularProgress from '@material-ui/core/CircularProgress';
import {
    TEACHER_SCHEDULE_LINK,
    HOME_PAGE_LINK,
    LOGIN_LINK,
    ADMIN_PAGE_LINK,
    SCHEDULE_PAGE_LINK,
    MY_PROFILE_LINK,
    TEACHER_LIST_LINK,
    LOGOUT_LINK,
} from '../../constants/links';

import LanguageSelector from '../LanguageSelector/LanguageSelector';
import * as colors from '../../constants/schedule/colors';

import FreeRooms from '../../containers/Dialogs/FreeRoomsDialog';
import { setSemesterLoadingService } from '../../services/loadingService';
import {
    LOGIN_TITLE,
    ADMIN_TITLE,
    SCHEDULE_TITLE,
    MY_PROFILE,
    LOGOUT_TITLE,
    SEMESTER_LABEL,
    HOME_TITLE,
    MENU_BUTTON,
} from '../../constants/translationLabels/common';
import { getCurrentSemesterRequsted } from '../../actions/schedule';

const StyledMenu = withStyles({
    paper: {
        border: `1px solid ${colors.colors.BORDER}`,
    },
})((props) => (
    <Menu
        elevation={0}
        getContentAnchorEl={null}
        anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'center',
        }}
        transformOrigin={{
            vertical: 'bottom',
            horizontal: 'center',
        }}
        {...props}
    />
));

const StyledMenuItem = withStyles((theme) => ({
    root: {
        '&:focus': {
            backgroundColor: theme.palette.primary.main,
            color: theme.palette.common.white,
        },
    },
}))(MenuItem);

const Header = (props) => {
    const { roles, userRole, loading, currentSemester, getCurrentSemester } = props;
    const [anchorEl, setAnchorEl] = useState(null);
    const handleClick = (event) => setAnchorEl(event.currentTarget);
    const handleClose = () => setAnchorEl(null);

    const [anchorElUser, setAnchorElUser] = useState(null);
    const handleClickUserMenu = (event) => setAnchorElUser(event.currentTarget);
    const handleCloseUserMenu = () => setAnchorElUser(null);

    const { t } = useTranslation('common');

    useEffect(() => {
        if (userRole === roles.MANAGER) {
            setSemesterLoadingService(true);
            getCurrentSemester();
        }
    }, [userRole]);

    const getUserMenu = (role) => {
        let userMenu = null;
        if (isNil(role)) {
            return (
                <Link to={LOGIN_LINK} className="navLinks">
                    {t(LOGIN_TITLE)}
                </Link>
            );
        }
        switch (role) {
            case roles.MANAGER:
                userMenu = (
                    <div className="user-menu">
                        <Button
                            aria-controls="customized-menu"
                            aria-haspopup="true"
                            variant="contained"
                            color="primary"
                            onClick={handleClickUserMenu}
                        >
                            {localStorage.getItem('email')}
                            <ListItemIcon>
                                <FaCaretDown fontSize="normall" />
                            </ListItemIcon>
                        </Button>
                        <StyledMenu
                            id="customized-menu"
                            anchorEl={anchorElUser}
                            keepMounted
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >
                            <Link
                                to={ADMIN_PAGE_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaUser fontSize="normall" />
                                    </ListItemIcon>
                                    {t(ADMIN_TITLE)}
                                </StyledMenuItem>
                            </Link>
                            <Link
                                to={SCHEDULE_PAGE_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaClock fontSize="normal" />
                                    </ListItemIcon>
                                    {t(SCHEDULE_TITLE)}
                                </StyledMenuItem>
                            </Link>
                            <span
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                                role="button"
                                tabIndex="0"
                            >
                                <StyledMenuItem>
                                    <FreeRooms classScheduler={props.classScheduler} />
                                </StyledMenuItem>
                            </span>
                            <Link
                                to={MY_PROFILE_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaUser fontSize="normal" />
                                    </ListItemIcon>
                                    {t(MY_PROFILE)}
                                </StyledMenuItem>
                            </Link>
                            <Link
                                to={LOGOUT_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaSignOutAlt fontSize="normal" />
                                    </ListItemIcon>
                                    {t(LOGOUT_TITLE)}
                                </StyledMenuItem>
                            </Link>
                        </StyledMenu>
                    </div>
                );
                break;
            case roles.TEACHER:
                userMenu = (
                    <div className="user-menu">
                        <Button
                            aria-controls="customized-menu"
                            aria-haspopup="true"
                            variant="contained"
                            color="primary"
                            onClick={handleClickUserMenu}
                        >
                            {localStorage.getItem('email')}
                            <ListItemIcon>
                                <FaCaretDown fontSize="normal" />
                            </ListItemIcon>
                        </Button>
                        <StyledMenu
                            id="customized-menu"
                            anchorEl={anchorElUser}
                            keepMounted
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >
                            <Link
                                to={TEACHER_LIST_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaClock fontSize="normal" />
                                    </ListItemIcon>
                                    {t(SCHEDULE_TITLE)}
                                </StyledMenuItem>
                            </Link>
                            <span
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={() => {
                                    handleCloseUserMenu();
                                }}
                                role="button"
                                tabIndex="0"
                            ></span>
                            <Link
                                to={MY_PROFILE_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaUser fontSize="normal" />
                                    </ListItemIcon>
                                    {t(MY_PROFILE)}
                                </StyledMenuItem>
                            </Link>
                            <Link
                                to={LOGOUT_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaSignOutAlt fontSize="normal" />
                                    </ListItemIcon>
                                    {t(LOGOUT_TITLE)}
                                </StyledMenuItem>
                            </Link>
                        </StyledMenu>
                    </div>
                );
                break;
            default:
                userMenu = (
                    <div className="user-menu">
                        <Button
                            aria-controls="customized-menu"
                            aria-haspopup="true"
                            variant="contained"
                            color="primary"
                            onClick={handleClickUserMenu}
                        >
                            {localStorage.getItem('email')}
                        </Button>
                        <StyledMenu
                            id="customized-menu"
                            anchorEl={anchorElUser}
                            keepMounted
                            open={Boolean(anchorElUser)}
                            onClose={handleCloseUserMenu}
                        >
                            <Link
                                to={LOGOUT_LINK}
                                className="navLinks"
                                style={{ textDecoration: 'none' }}
                                onClick={handleCloseUserMenu}
                            >
                                <StyledMenuItem>
                                    <ListItemIcon>
                                        <FaSignOutAlt fontSize="normal" />
                                    </ListItemIcon>
                                    {t(LOGOUT_TITLE)}
                                </StyledMenuItem>
                            </Link>
                        </StyledMenu>
                    </div>
                );
        }
        return userMenu;
    };

    let leftLinks = null;
    let menu = null;
    const userMenu = getUserMenu(userRole);
    if (userRole === roles.MANAGER) {
        leftLinks = (
            <>
                {loading ? (
                    <span className="navLinks nav-semester">
                        <CircularProgress size={20} />
                    </span>
                ) : (
                    <span className="navLinks nav-semester">
                        {t(SEMESTER_LABEL)}: {currentSemester.description}
                    </span>
                )}
            </>
        );
        menu = (
            <div className="menu">
                <Button
                    aria-controls="customized-menu"
                    aria-haspopup="true"
                    variant="contained"
                    color="primary"
                    onClick={handleClick}
                >
                    {t(MENU_BUTTON)}
                </Button>

                <StyledMenu
                    id="customized-menu"
                    anchorEl={anchorEl}
                    keepMounted
                    open={Boolean(anchorEl)}
                    onClose={handleClose}
                >
                    <span className="navLinks menu-semester">{currentSemester.description}</span>
                    <Link
                        to={HOME_PAGE_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaHome fontSize="normall" />
                            </ListItemIcon>
                            {t(HOME_TITLE)}
                        </StyledMenuItem>
                    </Link>

                    <Link
                        to={SCHEDULE_PAGE_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaClock fontSize="normall" />
                            </ListItemIcon>
                            {t(SCHEDULE_TITLE)}
                        </StyledMenuItem>
                    </Link>

                    <Link
                        to={ADMIN_PAGE_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaUser fontSize="normall" />
                            </ListItemIcon>
                            {t(ADMIN_TITLE)}
                        </StyledMenuItem>
                    </Link>

                    <span
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                        role="button"
                        tabIndex="0"
                    >
                        <StyledMenuItem>
                            <FreeRooms classScheduler={props.classScheduler} />
                        </StyledMenuItem>
                    </span>

                    <Link
                        to={LOGOUT_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaSignOutAlt fontSize="normall" />
                            </ListItemIcon>
                            {t(LOGOUT_TITLE)}
                        </StyledMenuItem>
                    </Link>
                </StyledMenu>
            </div>
        );
    } else if (userRole === roles.TEACHER) {
        menu = (
            <div className="menu">
                <Button
                    aria-controls="customized-menu"
                    aria-haspopup="true"
                    variant="contained"
                    color="primary"
                    onClick={handleClick}
                >
                    {t(MENU_BUTTON)}
                </Button>

                <StyledMenu
                    id="customized-menu"
                    anchorEl={anchorEl}
                    keepMounted
                    open={Boolean(anchorEl)}
                    onClose={handleClose}
                >
                    <Link
                        to={HOME_PAGE_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaHome fontSize="normall" />
                            </ListItemIcon>
                            {t(HOME_TITLE)}
                        </StyledMenuItem>
                    </Link>
                    <StyledMenuItem>
                        <ListItemIcon>
                            <FaClipboardList fontSize="normall" />
                        </ListItemIcon>
                    </StyledMenuItem>

                    <Link
                        to={TEACHER_SCHEDULE_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaClock fontSize="normall" />
                            </ListItemIcon>
                            {t(SCHEDULE_TITLE)}
                        </StyledMenuItem>
                    </Link>

                    <Link
                        to={LOGOUT_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaSignOutAlt fontSize="normall" />
                            </ListItemIcon>
                            {t(LOGOUT_TITLE)}
                        </StyledMenuItem>
                    </Link>
                </StyledMenu>
            </div>
        );
    } else if (isNil(userRole)) {
        menu = (
            <div className="menu">
                <Button
                    aria-controls="customized-menu"
                    aria-haspopup="true"
                    variant="contained"
                    color="primary"
                    onClick={handleClick}
                >
                    {t(MENU_BUTTON)}
                </Button>
                <StyledMenu
                    id="customized-menu"
                    anchorEl={anchorEl}
                    keepMounted
                    open={Boolean(anchorEl)}
                    onClose={handleClose}
                >
                    <Link
                        to={HOME_PAGE_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={() => {
                            setAnchorEl(null);
                        }}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaHome fontSize="normall" />
                            </ListItemIcon>
                            {t(HOME_TITLE)}
                        </StyledMenuItem>
                    </Link>
                    <Link
                        to={LOGIN_LINK}
                        className="navLinks"
                        style={{ textDecoration: 'none' }}
                        onClick={handleClose}
                    >
                        <StyledMenuItem>
                            <ListItemIcon>
                                <FaRunning fontSize="normall" />
                            </ListItemIcon>
                            {t(LOGIN_TITLE)}
                        </StyledMenuItem>
                    </Link>
                </StyledMenu>
            </div>
        );
    }

    return (
        <>
            <header className="header">
                {menu}
                <nav className="header-blocks header-blocks_one">
                    <Link to={HOME_PAGE_LINK} className="navLinks">
                        {t(HOME_TITLE)}
                    </Link>
                    {leftLinks}
                </nav>
                <nav className="header-blocks header-blocks_two">{userMenu}</nav>
                <nav className="header-blocks header-blocks_three">
                    <LanguageSelector />
                </nav>
            </header>
        </>
    );
};

const mapStateToProps = (state) => ({
    classScheduler: state.classActions.classScheduler,
    currentSemester: state.schedule.currentSemester,
    loading: state.loadingIndicator.semesterLoading,
});

const mapDispatchToProps = (dispatch) => ({
    getCurrentSemester: () => dispatch(getCurrentSemesterRequsted()),
});

export default connect(mapStateToProps, mapDispatchToProps)(Header);
