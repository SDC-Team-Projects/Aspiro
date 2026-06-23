import type { FormEvent, ReactNode } from "react";

type EditModalProps = {
  isOpen: boolean;
  title: string;
  submitText?: string;
  cancelText?: string;
  actionLoading?: boolean;
  children: ReactNode;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void | Promise<void>;
  onCancel: () => void;
};

export default function EditModal({
  isOpen,
  title,
  submitText = "Save changes",
  cancelText = "Cancel",
  actionLoading = false,
  children,
  onSubmit,
  onCancel,
}: EditModalProps) {
  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-overlay" onClick={onCancel}>
      <form
        className="modal-card edit-modal-card"
        onSubmit={onSubmit}
        onClick={(event) => event.stopPropagation()}
      >
        <h3 className="modal-title">{title}</h3>

        <div className="edit-modal-fields">{children}</div>

        <div className="modal-actions">
          <button
            type="button"
            className="modal-cancel-button"
            onClick={onCancel}
            disabled={actionLoading}
          >
            {cancelText}
          </button>

          <button
            type="submit"
            className="modal-confirm-button"
            disabled={actionLoading}
          >
            {actionLoading ? "Saving..." : submitText}
          </button>
        </div>
      </form>
    </div>
  );
}
