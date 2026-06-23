export type UserRole = "USER" | "ADMIN";

export type LoginRequest = {
  email: string;
  password: string;
};

export type RegisterRequest = {
  name: string;
  email: string;
  password: string;
};

export type AuthResponse = {
  accessToken: string;
  refreshToken?: string;
  expiresIn?: number;
  role: UserRole;
};