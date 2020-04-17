import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { Test2SharedModule } from 'app/shared/shared.module';
import { Test2CoreModule } from 'app/core/core.module';
import { Test2AppRoutingModule } from './app-routing.module';
import { Test2HomeModule } from './home/home.module';
import { Test2EntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { ReactiveFormsModule } from '@angular/forms';

@NgModule({
  imports: [
    BrowserModule,
    ReactiveFormsModule,
    Test2SharedModule,
    Test2CoreModule,
    Test2HomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    Test2EntityModule,
    Test2AppRoutingModule
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent]
})
export class Test2AppModule {}
