/*
 * Modifications copyright (C) 2019 Christian Chevalley, Vitasystems GmbH and Hannover Medical School

 * This file is part of Project EHRbase

 * Copyright (c) 2015 Christian Chevalley
 * This file is part of Project Ethercis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ehrbase.aql.containment;

/**
 * Container for containment definition<p>
 * This class is used to resolve a symbol in an AQL expression by associating its corresponding archetype Id,
 * class type (EHR, COMPOSITION etc.). Once resolved it holds the path for a given template.
 * </p>
 * Created by christian on 4/4/2016.
 */
public class Containment {

    private String symbol;
    private String archetypeId;
    private String className;
    private String path;
    private Containment enclosingContainment; //parent containment

    public Containment(Containment enclosingContainment) {
        this.enclosingContainment = enclosingContainment;
    }

    public Containment(String className, String symbol, String archetypeId) {
        this.setSymbol(symbol);
        this.archetypeId = archetypeOnly(archetypeId);
        this.className = className;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getArchetypeId() {
        return archetypeId;
    }

    public String getPath() {
        return path;
    }

    public String getClassName() {
        return className;
    }

    public Containment getEnclosingContainment() {
        return enclosingContainment;
    }



    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setArchetypeId(String archetypeId) {
        this.archetypeId = archetypeOnly(archetypeId);
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setEnclosingContainment(Containment enclosingContainment) {
        this.enclosingContainment = enclosingContainment;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(className == null ? "" : className);
        sb.append("::");
        sb.append(getSymbol() == null ? "" : getSymbol());
        sb.append("::");
        sb.append(archetypeId == null ? "" : archetypeId);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Containment that = (Containment) o;

        if (getSymbol() != null ? !getSymbol().equals(that.getSymbol()) : that.getSymbol() != null) return false;
        if (archetypeId != null ? !archetypeId.equals(that.archetypeId) : that.archetypeId != null) return false;
        if (className != null ? !className.equals(that.className) : that.className != null) return false;
        if (path != null ? !path.equals(that.path) : that.path != null) return false;
        return enclosingContainment != null ? enclosingContainment.equals(that.enclosingContainment) : that.enclosingContainment == null;
    }

    @Override
    public int hashCode() {
        int result = getSymbol() != null ? getSymbol().hashCode() : 0;
        result = 31 * result + (archetypeId != null ? archetypeId.hashCode() : 0);
        result = 31 * result + (className != null ? className.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (enclosingContainment != null ? enclosingContainment.hashCode() : 0);
        return result;
    }

    private String archetypeOnly(String archetypeId) {
        if (archetypeId != null && archetypeId.length() > 0 && archetypeId.contains("["))
            return archetypeId.substring(0, archetypeId.indexOf(']')).substring(1);
        else
            return archetypeId;
    }
}
