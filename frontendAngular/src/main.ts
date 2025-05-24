import { bootstrapApplication } from '@angular/platform-browser';
import { importProvidersFrom } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

// Import the routing configuration from routes.ts
import { appRoutes } from './app/app.routes';

// Import your components
import { AppComponent } from './app/app.component';  // Optional if you have a root component

bootstrapApplication(AppComponent, {
  providers: [
    importProvidersFrom(
      RouterModule.forRoot(appRoutes),  // Use the imported appRoutes for routing
      HttpClientModule                  // Import HttpClientModule for HTTP requests
    ),
  ],
})
  .catch((err) => console.error(err));
