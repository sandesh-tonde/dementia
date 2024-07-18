import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  return (
    <section className="home-services text-center">
      <div className="container">
        <div className="row">
          <div className="col-xs-12">
            <div className="title-box">
              <h2>Dementia Care</h2>
            </div>
          </div>

          <div className="col-md-3 col-sm-6 col-xs-12">
            <div className="content">
              <div className="line">
                <div className="icon">
                  <img src="content/images/msg.png" alt="Icon" onClick={handleClick} />{' '}
                </div>
              </div>
              <h3>ReliBot</h3>
            </div>
          </div>

          <div className="col-md-3 col-sm-6 col-xs-12">
            <div className="content">
              <div className="line">
                <div className="icon">
                  <img src="content/images/live.png" alt="Icon" />
                </div>
              </div>
              <h3>Nurse Connect</h3>
            </div>
          </div>

          <div className="col-md-3 col-sm-6 col-xs-12">
            <div className="content">
              <div className="line">
                <div className="icon">
                  <img src="content/images/comm.png" alt="Icon" />
                </div>
              </div>
              <h3>Community Connect</h3>
            </div>
          </div>

          <div className="col-md-3 col-sm-6 col-xs-12">
            <div className="content">
              <div className="icon">
                <img src="content/images/support.png" alt="Icon" />
              </div>
              <h3>Know More</h3>
            </div>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Home;

export const handleClick = () => {
  const element = document.getElementById('widgetIcon');
  element?.click();
};
