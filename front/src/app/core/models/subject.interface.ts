export interface Subject {
  id?: number;
  name: string;
  description: string;
  subscribed: Boolean;
  createdAt?: Date;
  updatedAt?: Date;
}
