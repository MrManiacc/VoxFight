/*
 * Copyright 2013 MovingBlocks
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
package me.jraynor.core.engine.processes;

import me.jraynor.core.context.Context;

/**
 *
 */
public abstract class SingleStepLoadProcess extends LoadProcessWithContext {
    public SingleStepLoadProcess(Context context) {
        super(context);
    }

    @Override
    public void begin() {
    }

    @Override
    public float getProgress() {
        return 0;
    }
}
