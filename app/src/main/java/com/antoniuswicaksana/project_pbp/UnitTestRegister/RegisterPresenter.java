//package com.antoniuswicaksana.project_pbp.UnitTestRegister;
//
//import com.tubesb.tubespbp.dao.UserDAO;
//
//public class RegisterPresenter {
//
//    private LoginView view;
//    private LoginService service;
//    private LoginCallback callback;
//    public RegisterPresenter(LoginView view, LoginService service) {
//        this.view = view;
//        this.service = service;
//    }
//    public void onLoginClicked() {
//        if (view.getEmail().isEmpty()) {
//            view.showEmailError("Email tidak boleh kosong");
//            return;
//        } else if (view.getPassword().isEmpty()) {
//            view.showPasswordError("Password tidak boleh kosong");
//            return;
//        } else {
//            service.login(view, view.getEmail(), view.getPassword(), new
//                    LoginCallback() {
//                        @Override
//                        public void onSuccess(boolean value, UserDAO user) {
//                            if (user.getNama().equalsIgnoreCase("admin")) {
//                                view.startMainActivity();
//                            } else {
//                                view.startUserProfileActivity(user);
//                            }
//                        }
//
//                        @Override
//                        public void onError() {
//                        }
//                    });
//            return;
//        }
//    }
//}
