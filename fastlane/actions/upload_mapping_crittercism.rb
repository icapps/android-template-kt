module Fastlane
  module Actions
    class UploadMappingCrittercismAction < Action
      def self.run(params)
        upload_file(params)
      end

      def self.upload_file(params)
        command = []
        command << "curl"
        command << verbose(params)
        command << upload_params(params)

        shell_command = command.join(' ')
		Helper.log.info "Uploading to crittercism using command: #{shell_command}"
        result = Helper.is_test? ? shell_command : `#{shell_command}`
        fail_on_error(result)
      end

      def self.fail_on_error(result)
        if result.include?("Successfully saved proguard file") == false
          UI.user_error!(result)
          UI.user_error!("Server error, failed to upload the mapping file")
        end
      end

      def self.upload_params(params)
        options = []
        options << make_url(params)
        options << '-F' << "proguard=@#{params[:mapping_file]}"
        options << '-F' << "app_version=\"#{params[:version_name]}\""
        options << '-F' << "key=\"#{params[:api_key]}\""
        options
      end

      def self.make_url(params)
        "https://app.crittercism.com/api_beta/proguard/#{params[:app_id]}"
      end

      def self.verbose(params)
        params[:verbose] ? "--verbose" : ""
      end

      #####################################################
      # @!group Documentation
      #####################################################

      def self.description
        "Upload proguard mapping file to Crittercism"
      end

      def self.available_options
        [
            FastlaneCore::ConfigItem.new(key: :mapping_file,
                                         env_name: "FL_MAPPING_FILE",
                                         description: "mapping.txt file to upload to Crittercism",
                                         optional: false),
            FastlaneCore::ConfigItem.new(key: :api_key,
                                         env_name: "FL_CRITTERCISM_API_KEY",
                                         description: "Crittercism App API key e.g. f57a57ca",
                                         optional: false),
            FastlaneCore::ConfigItem.new(key: :app_id,
                                         env_name: "FL_CRITTERCISM_APP_ID",
                                         description: "Crittercism App ID e.g. e05ba40754c4869fb7e0b61",
                                         optional: false),
            FastlaneCore::ConfigItem.new(key: :version_name,
                                         env_name: "FL_VERSION_NAME",
                                         description: "Application version name. e.g. 1.0.15",
                                         optional: false),
            FastlaneCore::ConfigItem.new(key: :verbose,
                                         env_name: "FL_CRITTERCISM_VERBOSE",
                                         description: "Make detailed output",
                                         is_string: false,
                                         default_value: false,
                                         optional: true)
        ]
      end

      def self.authors
        ["nicolaverbeeck"]
      end

      def self.is_supported?(platform)
        platform == :android
      end
    end
  end
end
