import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IBlog {
  id?: number;
  content?: string;
  dateTime?: dayjs.Dayjs;
  vote?: number | null;
  auther?: IUser | null;
}

export const defaultValue: Readonly<IBlog> = {};
