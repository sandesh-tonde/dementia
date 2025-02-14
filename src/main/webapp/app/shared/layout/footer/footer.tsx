import './footer.scss';

import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';

const Footer = () => (
  <div className="footer page-content">
    <Row>
      <Col md="12">
        <p>© 2024 Dementia Care</p>
      </Col>
    </Row>
  </div>
);

export default Footer;
