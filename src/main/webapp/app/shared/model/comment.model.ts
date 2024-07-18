import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IBlog } from 'app/shared/model/blog.model';

export interface IComment {
  id?: number;
  commentText?: string | null;
  dateTime?: dayjs.Dayjs | null;
  addedBy?: IUser | null;
  blog_comments?: IBlog | null;
}

export const defaultValue: Readonly<IComment> = {};
