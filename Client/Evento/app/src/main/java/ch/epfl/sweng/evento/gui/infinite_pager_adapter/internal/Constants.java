/*
 * Copyright (C) 2013 Onur-Hayri Bakici
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

package ch.epfl.sweng.evento.gui.infinite_pager_adapter.internal;

public final class Constants {

    public static final int PAGE_POSITION_LEFT = 0;
    public static final int PAGE_POSITION_CENTER = 1;
    public static final int PAGE_POSITION_RIGHT = 2;
    public static final int PAGE_COUNT = 3;
    public static final String SUPER_STATE = "super_state";
    public static final String ADAPTER_STATE = "adapter_state";
    public static final String LOG_TAG = "InfiniteViewPager";
    public static boolean DEBUG = false;

    private Constants() {
        // nop
    }
}