import React, { Suspense, useEffect } from 'react';
import './i18n';
import { connect } from 'react-redux';

import Routers from './router/Routers';
import { authCheckState } from './actions/index';
import { handleSnackbarCloseService } from './services/snackbarService';
import SnackbarComponent from './share/Snackbar/SnackbarComponent';
import SuccessSnackbar from './components/SuccessSnackbar/SuccessSnackbar';
import './App.scss';

export function App(props) {
  const { isSnackbarOpen, snackbarType, snackbarMessage } = props;
  const handleSnackbarClose = (event, reason) => {
    if (!reason === 'clickaway') {
      return;
    }

    handleSnackbarCloseService();
  };

  useEffect(() => {
    props.onTryAutoLogin();
  }, [props]);

  return (
    <Suspense fallback={null}>
      <div className="container">
        <Routers />
        <SuccessSnackbar />
        <SnackbarComponent
          message={snackbarMessage}
          type={snackbarType}
          isOpen={isSnackbarOpen}
          handleSnackbarClose={handleSnackbarClose}
        />
      < /div>
        < /Suspense>
    );
}

const mapStateToProps = (state) => ({
    isSnackbarOpen: state.snackbar.isSnackbarOpen,
    snackbarType: state.snackbar.snackbarType,
    snackbarMessage: state.snackbar.message,
});

const mapDispatchToProps = (dispatch) => {
    return {
        onTryAutoLogin: () => dispatch(authCheckState()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(App);
