/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nero_soft.com.nero_soft;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import nero_soft.com.nero_soft.ViewHolder.DashBoard;
import nero_soft.com.nero_soft.ViewHolder.SmartHome;



public abstract class Contrdol {


    public static class SH extends Fragment {
        public SmartHome smartHome;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //-----------------infat contanir for pager
            this.smartHome = new SmartHome(getActivity(), inflater, container);
            this.smartHome.setupRecyclerView();
            this.smartHome.setUpSettingView();
            return this.smartHome.getView();
        }
    }

    public static class DB extends Fragment {
        public DashBoard dashBoard;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //-----------------infat contanir for pager
            dashBoard = new DashBoard(getActivity().getFragmentManager(),inflater, container);
            dashBoard.setUpDashBoardView();
            dashBoard.setUpButtonDashBord();
            dashBoard.setUpComponant();
            dashBoard.setUpPirSinsor();
            dashBoard.setUpInfoDash();
            return this.dashBoard.getView();
        }
    }





}

