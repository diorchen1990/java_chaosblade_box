export interface LoginForm {
  username: string;
  password: string;
}

export interface LoginResponse {
  token: string;
}

export interface UserInfo {
  username: string;
  roles: string[];
} 