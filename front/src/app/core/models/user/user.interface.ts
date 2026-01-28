export interface User {
  id: number;
  email: string;
  userName: string;
  password: string;
  createdAt: Date;
  updatedAt?: Date;
}
