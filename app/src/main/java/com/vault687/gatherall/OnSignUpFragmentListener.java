package com.vault687.gatherall;

/**
 * Created by kiwil_000 on 12/22/2014.
 */

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 * <p/>
 * See the Android Training lesson <a href=
 * "http://developer.android.com/training/basics/fragments/communicating.html"
 * >Communicating with Other Fragments</a> for more information.
 */
public interface OnSignUpFragmentListener {
    public void onUsernameAndPhoneEntered(String username, String phone);
    public void onPasswordObtained(String username, String password);
}