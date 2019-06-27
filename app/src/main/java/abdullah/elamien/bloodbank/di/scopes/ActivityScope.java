package abdullah.elamien.bloodbank.di.scopes;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;

import javax.inject.Scope;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by AbdullahAtta on 6/18/2019.
 */
@Scope
@Documented
@Retention(RUNTIME)
public @interface ActivityScope {
}
