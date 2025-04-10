import { bootstrapApplication } from '@angular/platform-browser';
import { importProvidersFrom } from '@angular/core';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';

// Import the routing configuration from routes.ts
import { appRoutes } from './app/app.routes';

// Import your components
<<<<<<< HEAD
import { LoginComponent } from './app/components/login/login.component'; // Adjust the path to your LoginComponent
=======
import { LoginComponent } from './app/login/login.component';  // Adjust the path to your LoginComponent
>>>>>>> db9f408 (Latest Changes)
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
