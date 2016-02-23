/*
 * Copyright 2007-2012 the original author or authors.
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
package aajb.domain.school;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;


/**
 * Annotation configured person student bean.
 * 
 * @author David Winterfeldt
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue(value="ST")
public class Student extends Person {

    private String className;
    @Column
    private LocalDate birthDate;
    private String remarks;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Parent firstParent;
    @ManyToOne(cascade = CascadeType.REFRESH)
    private Parent secondParent;
    @ManyToMany
    @JoinTable(name = "STUDENT_PARENTS",
            joinColumns = {@JoinColumn(nullable = false, updatable = false, name = "STUDENT_ID")},
            inverseJoinColumns = {@JoinColumn(nullable = false, updatable = false, name = "PARENT_ID")})
    private Set<Parent> otherParents;

    
}
