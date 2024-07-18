import { IUser } from 'app/shared/model/user.model';

export interface IPatient {
  id?: number;
  firstName?: string;
  lastName?: string;
  age?: number;
  email?: string;
  gender?: string | null;
  relationship?: string;
  stage?: string;
  caregiverNotes?: string | null;
  primaryConcerns?: string | null;
  medicalHistory?: string | null;
  medicationDetails?: string | null;
  emergencyContact?: string;
  user_petient?: IUser | null;
}

export const defaultValue: Readonly<IPatient> = {};
