import patient from 'app/entities/patient/patient.reducer';
import comment from 'app/entities/comment/comment.reducer';
import blog from 'app/entities/blog/blog.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  patient,
  comment,
  blog,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
