/**
 * Employee Management System - Main JavaScript File
 */

// Global variables
let currentPage = 0;
let currentSize = 10;
let currentSortBy = 'firstName';
let currentSortDir = 'asc';

// DOM Ready
document.addEventListener('DOMContentLoaded', function() {
    initializeApplication();
});

/**
 * Initialize the application
 */
function initializeApplication() {
    console.log('Employee Management System initialized');
    
    // Initialize form validation
    initializeFormValidation();
    
    // Initialize search functionality
    initializeSearch();
    
    // Initialize table sorting
    initializeTableSorting();
    
    // Initialize pagination
    initializePagination();
    
    // Initialize modals
    initializeModals();
    
    // Initialize tooltips
    initializeTooltips();
    
    // Initialize auto-save functionality
    initializeAutoSave();
}

/**
 * Initialize form validation
 */
function initializeFormValidation() {
    const forms = document.querySelectorAll('form[data-validate]');
    
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            if (!validateForm(this)) {
                e.preventDefault();
                showValidationErrors(this);
            }
        });
        
        // Real-time validation
        const inputs = form.querySelectorAll('input, select, textarea');
        inputs.forEach(input => {
            input.addEventListener('blur', function() {
                validateField(this);
            });
            
            input.addEventListener('input', function() {
                clearFieldError(this);
            });
        });
    });
}

/**
 * Validate a single form field
 */
function validateField(field) {
    const value = field.value.trim();
    const rules = field.dataset.validation;
    
    if (!rules) return true;
    
    let isValid = true;
    let errorMessage = '';
    
    // Required validation
    if (rules.includes('required') && !value) {
        isValid = false;
        errorMessage = 'This field is required';
    }
    
    // Email validation
    if (rules.includes('email') && value && !isValidEmail(value)) {
        isValid = false;
        errorMessage = 'Please enter a valid email address';
    }
    
    // Phone validation
    if (rules.includes('phone') && value && !isValidPhone(value)) {
        isValid = false;
        errorMessage = 'Please enter a valid phone number';
    }
    
    // Salary validation
    if (rules.includes('salary') && value) {
        const salary = parseFloat(value);
        if (isNaN(salary) || salary < 20000 || salary > 500000) {
            isValid = false;
            errorMessage = 'Salary must be between $20,000 and $500,000';
        }
    }
    
    // Date validation
    if (rules.includes('date') && value) {
        const selectedDate = new Date(value);
        const today = new Date();
        if (selectedDate > today) {
            isValid = false;
            errorMessage = 'Date cannot be in the future';
        }
    }
    
    if (!isValid) {
        showFieldError(field, errorMessage);
    } else {
        clearFieldError(field);
    }
    
    return isValid;
}

/**
 * Validate entire form
 */
function validateForm(form) {
    const fields = form.querySelectorAll('input, select, textarea');
    let isValid = true;
    
    fields.forEach(field => {
        if (!validateField(field)) {
            isValid = false;
        }
    });
    
    return isValid;
}

/**
 * Show field error
 */
function showFieldError(field, message) {
    clearFieldError(field);
    
    field.classList.add('is-invalid');
    
    const errorDiv = document.createElement('div');
    errorDiv.className = 'invalid-feedback';
    errorDiv.textContent = message;
    
    field.parentNode.appendChild(errorDiv);
}

/**
 * Clear field error
 */
function clearFieldError(field) {
    field.classList.remove('is-invalid');
    
    const errorDiv = field.parentNode.querySelector('.invalid-feedback');
    if (errorDiv) {
        errorDiv.remove();
    }
}

/**
 * Show validation errors summary
 */
function showValidationErrors(form) {
    const errors = form.querySelectorAll('.is-invalid');
    
    if (errors.length > 0) {
        showAlert('Please correct the errors in the form', 'danger');
        
        // Scroll to first error
        errors[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
}

/**
 * Email validation helper
 */
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

/**
 * Phone validation helper
 */
function isValidPhone(phone) {
    const phoneRegex = /^\+?1?[-.\s]?\(?([0-9]{3})\)?[-.\s]?([0-9]{3})[-.\s]?([0-9]{4})$/;
    return phoneRegex.test(phone);
}

/**
 * Initialize search functionality
 */
function initializeSearch() {
    const searchForm = document.querySelector('form[action*="search"]');
    if (!searchForm) return;
    
    const searchInput = searchForm.querySelector('input[name="keyword"]');
    if (!searchInput) return;
    
    let searchTimeout;
    
    searchInput.addEventListener('input', function() {
        clearTimeout(searchTimeout);
        const keyword = this.value.trim();
        
        if (keyword.length >= 2) {
            searchTimeout = setTimeout(() => {
                performSearch(keyword);
            }, 500);
        }
    });
    
    // Search form submission
    searchForm.addEventListener('submit', function(e) {
        e.preventDefault();
        const keyword = searchInput.value.trim();
        if (keyword) {
            performSearch(keyword);
        }
    });
}

/**
 * Perform search operation
 */
function performSearch(keyword) {
    console.log('Searching for:', keyword);
    
    // Show loading state
    showLoadingState();
    
    // Perform AJAX search
    fetch(`/api/employees/search?keyword=${encodeURIComponent(keyword)}`)
        .then(response => response.json())
        .then(data => {
            updateSearchResults(data);
        })
        .catch(error => {
            console.error('Search error:', error);
            showAlert('Error performing search', 'danger');
        })
        .finally(() => {
            hideLoadingState();
        });
}

/**
 * Update search results
 */
function updateSearchResults(results) {
    // Implementation depends on your UI structure
    console.log('Search results:', results);
}

/**
 * Initialize table sorting
 */
function initializeTableSorting() {
    const sortableHeaders = document.querySelectorAll('th[data-sort]');
    
    sortableHeaders.forEach(header => {
        header.addEventListener('click', function() {
            const sortBy = this.dataset.sort;
            const currentDir = this.dataset.direction || 'asc';
            const newDir = currentDir === 'asc' ? 'desc' : 'asc';
            
            // Update all headers
            sortableHeaders.forEach(h => {
                h.dataset.direction = '';
                h.classList.remove('sort-asc', 'sort-desc');
            });
            
            // Update current header
            this.dataset.direction = newDir;
            this.classList.add(newDir === 'asc' ? 'sort-asc' : 'sort-desc');
            
            // Perform sort
            sortTable(sortBy, newDir);
        });
    });
}

/**
 * Sort table data
 */
function sortTable(sortBy, direction) {
    currentSortBy = sortBy;
    currentSortDir = direction;
    
    // Reload current page with new sorting
    reloadCurrentPage();
}

/**
 * Initialize pagination
 */
function initializePagination() {
    const paginationLinks = document.querySelectorAll('.pagination .page-link');
    
    paginationLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            const page = this.dataset.page;
            if (page !== undefined) {
                currentPage = parseInt(page);
                reloadCurrentPage();
            }
        });
    });
}

/**
 * Reload current page with current parameters
 */
function reloadCurrentPage() {
    const url = new URL(window.location);
    url.searchParams.set('page', currentPage);
    url.searchParams.set('size', currentSize);
    url.searchParams.set('sortBy', currentSortBy);
    url.searchParams.set('sortDir', currentSortDir);
    
    window.location.href = url.toString();
}

/**
 * Initialize modals
 */
function initializeModals() {
    // Delete confirmation modal
    const deleteButtons = document.querySelectorAll('[data-action="delete"]');
    
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            const employeeId = this.dataset.employeeId;
            const employeeName = this.dataset.employeeName;
            
            showDeleteConfirmation(employeeId, employeeName);
        });
    });
}

/**
 * Show delete confirmation modal
 */
function showDeleteConfirmation(employeeId, employeeName) {
    const modal = document.getElementById('deleteModal');
    if (!modal) return;
    
    // Update modal content
    const nameElement = modal.querySelector('#employeeName');
    if (nameElement) {
        nameElement.textContent = employeeName;
    }
    
    // Update form action
    const form = modal.querySelector('#deleteForm');
    if (form) {
        form.action = `/employees/${employeeId}/delete`;
    }
    
    // Show modal
    const bootstrapModal = new bootstrap.Modal(modal);
    bootstrapModal.show();
}

/**
 * Initialize tooltips
 */
function initializeTooltips() {
    const tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize auto-save functionality
 */
function initializeAutoSave() {
    const forms = document.querySelectorAll('form[data-autosave]');
    
    forms.forEach(form => {
        const inputs = form.querySelectorAll('input, select, textarea');
        let autoSaveTimeout;
        
        inputs.forEach(input => {
            input.addEventListener('input', function() {
                clearTimeout(autoSaveTimeout);
                autoSaveTimeout = setTimeout(() => {
                    autoSaveForm(form);
                }, 2000);
            });
        });
    });
}

/**
 * Auto-save form data
 */
function autoSaveForm(form) {
    const formData = new FormData(form);
    const data = Object.fromEntries(formData);
    
    // Save to localStorage
    const formId = form.id || 'employeeForm';
    localStorage.setItem(`autosave_${formId}`, JSON.stringify(data));
    
    console.log('Form auto-saved');
}

/**
 * Restore auto-saved form data
 */
function restoreAutoSavedData(form) {
    const formId = form.id || 'employeeForm';
    const savedData = localStorage.getItem(`autosave_${formId}`);
    
    if (savedData) {
        try {
            const data = JSON.parse(savedData);
            
            Object.keys(data).forEach(key => {
                const field = form.querySelector(`[name="${key}"]`);
                if (field && !field.value) {
                    field.value = data[key];
                }
            });
            
            console.log('Auto-saved data restored');
        } catch (error) {
            console.error('Error restoring auto-saved data:', error);
        }
    }
}

/**
 * Show alert message
 */
function showAlert(message, type = 'info', duration = 5000) {
    const alertContainer = document.getElementById('alertContainer') || createAlertContainer();
    
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    
    alertContainer.appendChild(alertDiv);
    
    // Auto-remove after duration
    if (duration > 0) {
        setTimeout(() => {
            if (alertDiv.parentNode) {
                alertDiv.remove();
            }
        }, duration);
    }
    
    // Scroll to alert
    alertDiv.scrollIntoView({ behavior: 'smooth', block: 'center' });
}

/**
 * Create alert container if it doesn't exist
 */
function createAlertContainer() {
    const container = document.createElement('div');
    container.id = 'alertContainer';
    container.className = 'position-fixed top-0 end-0 p-3';
    container.style.zIndex = '9999';
    
    document.body.appendChild(container);
    return container;
}

/**
 * Show loading state
 */
function showLoadingState() {
    const loadingOverlay = document.createElement('div');
    loadingOverlay.id = 'loadingOverlay';
    loadingOverlay.className = 'position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center';
    loadingOverlay.style.cssText = 'background: rgba(0,0,0,0.5); z-index: 9999;';
    loadingOverlay.innerHTML = `
        <div class="spinner-border text-light" role="status">
            <span class="visually-hidden">Loading...</span>
        </div>
    `;
    
    document.body.appendChild(loadingOverlay);
}

/**
 * Hide loading state
 */
function hideLoadingState() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
}

/**
 * Format currency
 */
function formatCurrency(amount) {
    return new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD'
    }).format(amount);
}

/**
 * Format date
 */
function formatDate(date) {
    return new Intl.DateTimeFormat('en-US', {
        year: 'numeric',
        month: 'long',
        day: 'numeric'
    }).format(new Date(date));
}

/**
 * Calculate tenure
 */
function calculateTenure(hireDate) {
    const hire = new Date(hireDate);
    const today = new Date();
    
    const years = today.getFullYear() - hire.getFullYear();
    const months = today.getMonth() - hire.getMonth();
    
    if (months < 0) {
        return `${years - 1} years, ${12 + months} months`;
    }
    
    return `${years} years, ${months} months`;
}

/**
 * Export employee data
 */
function exportEmployeeData(format = 'csv') {
    const url = `/api/employees/export?format=${format}`;
    
    fetch(url)
        .then(response => response.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `employees.${format}`;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            a.remove();
        })
        .catch(error => {
            console.error('Export error:', error);
            showAlert('Error exporting data', 'danger');
        });
}

/**
 * Print employee details
 */
function printEmployeeDetails() {
    window.print();
}

/**
 * Send email to employee
 */
function sendEmailToEmployee(employeeId, email) {
    const subject = encodeURIComponent('Employee Management System - Important Information');
    const body = encodeURIComponent('Hello,\n\nThis is a message from the Employee Management System.\n\nBest regards,\nHR Team');
    
    window.open(`mailto:${email}?subject=${subject}&body=${body}`);
}

// Utility functions
const Utils = {
    debounce: function(func, wait) {
        let timeout;
        return function executedFunction(...args) {
            const later = () => {
                clearTimeout(timeout);
                func(...args);
            };
            clearTimeout(timeout);
            timeout = setTimeout(later, wait);
        };
    },
    
    throttle: function(func, limit) {
        let inThrottle;
        return function() {
            const args = arguments;
            const context = this;
            if (!inThrottle) {
                func.apply(context, args);
                inThrottle = true;
                setTimeout(() => inThrottle = false, limit);
            }
        };
    },
    
    generateUUID: function() {
        return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
            const r = Math.random() * 16 | 0;
            const v = c == 'x' ? r : (r & 0x3 | 0x8);
            return v.toString(16);
        });
    }
};

// Export functions for global use
window.EmployeeMS = {
    showAlert,
    formatCurrency,
    formatDate,
    calculateTenure,
    exportEmployeeData,
    printEmployeeDetails,
    sendEmailToEmployee,
    Utils
};


